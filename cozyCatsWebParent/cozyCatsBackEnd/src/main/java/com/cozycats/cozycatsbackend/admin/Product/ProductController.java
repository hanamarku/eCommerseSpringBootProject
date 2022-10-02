package com.cozycats.cozycatsbackend.admin.Product;

import Exceptions.ProductNotFoundException;
import com.cozycats.cozycatsbackend.admin.Brand.BrandNotFoundException;
import com.cozycats.cozycatsbackend.admin.Brand.BrandService;
import com.cozycats.cozycatsbackend.admin.Category.CategoryService;
import com.cozycats.cozycatsbackend.admin.FileUploadUtil;
import com.cozycats.cozycatsbackend.admin.security.CozyCatsUserDetails;
import com.cozycats.cozycatscommon.entity.Brand;
import com.cozycats.cozycatscommon.entity.Category;
import com.cozycats.cozycatscommon.entity.Product;
import com.cozycats.cozycatscommon.entity.ProductImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.Transient;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class ProductController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/products")
    public String listFirstPage(Model model) {
        return listByPage(1, model, "name", "asc", null, 0);
    }

    @GetMapping("/products/page/{pageNum}")
    public String listByPage(
            @PathVariable(name = "pageNum") int pageNum, Model model,
            @Param("sortField") String sortField, @Param("sortDir") String sortDir,
            @Param("keyword") String keyword,
            @Param("categoryId") Integer categoryId
    ) {
        Page<Product> page = productService.listByPage(pageNum, sortField, sortDir, keyword, categoryId);
        List<Product> listProducts = page.getContent();

        List<Category> listCategories = categoryService.listCategoriesUsedInForm();

        long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
        long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        if(categoryId != null) model.addAttribute("categoryId", categoryId);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("listCategories", listCategories);

        return "products/products";
    }

    @GetMapping("/products/new")
    public String newProduct(Model model) {
        List<Brand> listBrands = brandService.listAll();
        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);
        model.addAttribute("product", product);
        model.addAttribute("listBrands", listBrands);
        model.addAttribute("pageTitle", "Create New Product");
        model.addAttribute("numberOfExistingExtraImages", 0);

        return "products/product_form";
    }

    @PostMapping("/products/save")
    public String saveProduct(Product product, RedirectAttributes ra,
                              @RequestParam(value = "fileImage", required = false) MultipartFile mainImageMultipart,
                              @RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultiparts,
                              @RequestParam(name = "detailIDs", required = false) String[] detailIDs,
                              @RequestParam(name = "detailNames", required = false) String[] detailNames,
                              @RequestParam(name = "detailValues", required = false) String[] detailValues,
                              @RequestParam(name = "imageIDs", required = false) String[] imageIDs,
                              @RequestParam(name = "imageNames", required = false) String[] imageNames,
                              @AuthenticationPrincipal CozyCatsUserDetails loggerUser) throws IOException {
        if(loggerUser.hasRole("SalesPerson")){
            productService.saveProductPrice(product);
            ra.addFlashAttribute("message", "The product has been saved successfully");
            return "redirect:/products";
        }
        setMainImageName(mainImageMultipart, product);
        setExistingExtraImageNames(imageIDs, imageNames, product);
        setNewExtraImageNames(extraImageMultiparts, product);
        setProductDetails(detailIDs, detailNames, detailValues, product);

        Product savedProduct = productService.save(product);
        saveUploadedImages(mainImageMultipart, extraImageMultiparts, savedProduct);
        deleteExtraImagesRemovedOnForm(product);

        ra.addFlashAttribute("message", "The product has been saved successfully");
        return "redirect:/products";
    }

    private void deleteExtraImagesRemovedOnForm(Product product) {
        String extraImage = "../product-images/" + product.getId() + "/extras";
        Path dirPath = Paths.get(extraImage);
        try{
            Files.list(dirPath).forEach(file -> {
                String filename = file.toFile().getName();
                LOGGER.info("Deleted extra image : " + filename);
                if(!product.containsImageName(filename)){
                    try{
                        Files.delete(file);
                    }catch (IOException ex){
                        LOGGER.error("Could not delete extra image : " + filename);
                    }
                }
            });

        }catch (IOException ex){
            LOGGER.error("Could not list directory : " + dirPath);
        }
    }

    private void setExistingExtraImageNames(String[] imageIDs, String[] imageNames, Product product) {
        if(imageIDs == null || imageIDs.length == 0) return;
        Set<ProductImage> images = new HashSet<>();
        for(int count = 0; count < imageIDs.length;count++){
            Integer id = Integer.parseInt(imageIDs[count]);
            String name = imageNames[count];
            images.add(new ProductImage(id, name, product));
        }
        product.setImages(images);
    }

    private void setProductDetails(String[] detailIDs, String[] detailNames,
                                   String[] detailValues, Product product) {
        if (detailNames == null || detailNames.length == 0) return;

        for (int count = 0; count < detailNames.length; count++) {
            String name = detailNames[count];
            String value = detailValues[count];
            Integer id = Integer.parseInt(detailIDs[count]);

            if (id != 0) {
                product.addDetail(id, name, value);
            } else if (!name.isEmpty() && !value.isEmpty()) {
                product.addDetail(name, value);
            }
        }
    }

    private void saveUploadedImages(MultipartFile mainImageMultipart, MultipartFile[] extraImageMultiparts, Product savedProduct) throws IOException {
            if (!mainImageMultipart.isEmpty()) {
                String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());

                String uploadDir = "../product-images/" + savedProduct.getId();

                FileUploadUtil.cleanDir(uploadDir);
                FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
            }
            if (extraImageMultiparts.length > 0) {
                String uploadDir = "../product-images/" + savedProduct.getId() + "/extras";
                for (MultipartFile multipartFile : extraImageMultiparts) {
                    if (multipartFile.isEmpty()) continue;
                        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
                }
            }
    }


    private void setNewExtraImageNames(MultipartFile[] extraImageMultiparts, Product product)
    {
            if(extraImageMultiparts.length > 0){
                for(MultipartFile multipartFile : extraImageMultiparts){
                    if(!multipartFile.isEmpty()){
                        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                        if(!product.containsImageName(fileName)) {
                            product.addExtraImages(fileName);
                        }
                    }
                }
            }
    }

    private void setMainImageName(MultipartFile mainImageMultipart, Product product) {
        if (!mainImageMultipart.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
            product.setMainImage(fileName);
        }
    }


    @GetMapping("/products/{id}/enabled/{status}")
    public String updateCategoryEnabledStatus(@PathVariable("id") Integer id,
                                              @PathVariable("status") boolean enabled, RedirectAttributes redirectAttributes) {
        productService.updateEnabledStatus(id, enabled);

        String status = enabled ? "enabled" : "disabled";
        String message = "The product ID " + id + " has been " + status;
        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/products";
    }

    @GetMapping("/products/delete/{id}")
    public String deleteBrand(@PathVariable(name = "id") Integer id,
                              Model model,
                              RedirectAttributes redirectAttributes) throws ProductNotFoundException {
        productService.delete(id);

        String productExtraImagesDir = "../product-images/" +id + "/extras";
        FileUploadUtil.removeDir(productExtraImagesDir);

        String productImagesDir = "../product-images/" +id;
        FileUploadUtil.removeDir(productImagesDir);

        redirectAttributes.addFlashAttribute("message",
                "The brand ID " + id + " has been deleted successfully");

        return "redirect:/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProducts(@PathVariable("id") Integer id, Model model,
                               RedirectAttributes ra) throws ProductNotFoundException {
        Product product = productService.get(id);
        List<Brand> listBrands = brandService.listAll();
        Integer numberOfExistingExtraImages = product.getImages().size();

        model.addAttribute("product", product);
        model.addAttribute("listBrands", listBrands);
        model.addAttribute("PageTitle", "Edit Product Id :" + id);
        model.addAttribute("numberOfExistingExtraImages", numberOfExistingExtraImages);

        return "products/product_form";
    }

    @GetMapping("/products/detail/{id}")
    public String viewProductDetails(@PathVariable("id") Integer id, Model model,
                                     RedirectAttributes ra) throws ProductNotFoundException {
        Product product = productService.get(id);
        model.addAttribute("product", product);

        return "products/product_detail_modal";

    }




}
