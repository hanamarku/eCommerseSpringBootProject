$(document).ready(function(){
    $("#buttonCancel").on("click", function(){
        window.location =  moduleURL;
    });

    $("#fileImage").change(function(){
        fileSize = this.files[0].size;
        if(fileSize > 5000000){
            this.setCustomValidity("Ypu must chose an image less than 5 MB");
            this.reportValidity();
        }else{
            showImageThumbnail(this);
        }
    });
});
function showImageThumbnail(fileInput) {
    var file = fileInput.files[0];
    var reader = new FileReader();
    reader.onload = function (e) {
        $("#thumbnail").attr("src", e.target.result);
    };
    reader.readAsDataURL(file);
}
