var buttonLoad;
var dropDownCountry;
var buttonAddCountry;
var buttonDeleteCountry;
var buttonUpdateCountry;
var labelCountryName;
var fieldCountryName;
var fieldCountryCode;

function loadCountries() {
    url = contextPath + "countries/list";
    $.get(url, function (responseJSON){
        dropDownCountry.empty();
        $.each(responseJSON, function (index, country){
            optionValue = country.id + "-" + country.code;

            $("<option>").val(optionValue).text(country.name).appendTo(dropDownCountry);
        });
    }).done(function (){
        buttonLoad.val("Refresh Country List");
        showToastMessage("All coutries have been loaded");
    }).fail(function (){
        showToastMessage("Error : Could not load the countries");
    })
}

function changeFormStateToSelectedCountry() {
    buttonAddCountry.prop("value", "New");
    buttonUpdateCountry.prop("disabled", false);
    buttonDeleteCountry.prop("disabled", false);

    labelCountryName.text("Selected Country: ");
    selectedCountryName = $("#dropDownCountries option:selected").text();
    fieldCountryName.val(selectedCountryName);
    countryCode = dropDownCountry.val().split("-")[1];
    fieldCountryCode.val(countryCode);

}

function changeFormStateToNew() {
    buttonAddCountry.val("Add");
    labelCountryName.text("Country Name");
    buttonUpdateCountry.prop("disabled", true);
    buttonDeleteCountry.prop("disabled", true);
    fieldCountryName.val("").focus();
    fieldCountryCode.val("");
}

function selectNewlyAddedCountry(countryId, countryCode, countryName) {
    optionValue = countryId + "-" + countryCode + "-" + countryName;
    $("<option>").val(optionValue).text(countryName).appendTo(dropDownCountry);
    $("#dropDownCountries option[value='" + optionValue + "']").prop("selected", true);

    fieldCountryCode.val("");
    fieldCountryName.val("").focus();
}

function addCountry() {
    url = contextPath + "countries/save";
    countryName = fieldCountryName.val();
    countryCode = fieldCountryCode.val();
    jsonData = {name: countryName, code:countryCode};
    $.ajax({
        type: "POST",
        url: url,
        beforeSend: function (xhr){
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function (countryId){
        selectNewlyAddedCountry(countryId, countryCode, countryName);
        showToastMessage("The new country has been added")
    });
}

function updateCountry() {
    url = contextPath + "countries/save";
    countryName = fieldCountryName.val();
    countryCode = fieldCountryCode.val();
    countryId = dropDownCountry.val().split("-")[0];
    jsonData = {id: countryId, name: countryName, code: countryCode};

    $.ajax({
        type: 'POST',
        url: url,
        beforeSend: function(xhr) {
            xhr.setRequestHeader(csrfHeaderName, csrfValue);
        },
        data: JSON.stringify(jsonData),
        contentType: 'application/json'
    }).done(function(countryId) {
        $("#dropDownCountries option:selected").val(countryId + "-" + countryCode);
        $("#dropDownCountries option:selected").text(countryName);
        showToastMessage("The country has been updated");

        changeFormStateToNew();
    }).fail(function() {
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    });
}

function deletCountry() {
    optionValue = dropDownCountry.val();
    countryId = optionValue.split("-")[0];

    url = contextPath + "countries/delete/" + countryId;

    $.get(url, function() {
        $("#dropDownCountries option[value='" + optionValue + "']").remove();
        changeFormStateToNew();
    }).done(function() {
        showToastMessage("The country has been deleted");
    }).fail(function() {
        showToastMessage("ERROR: Could not connect to server or server encountered an error");
    });
}

$(document).ready(function (){
   buttonLoad = $("#buttonLoadCountries");
   dropDownCountry = $("#dropDownCountries");

   buttonAddCountry = $("#buttonAddCountry");
   buttonDeleteCountry = $("#buttonDeleteCountry");
   buttonUpdateCountry = $("#buttonUpdateCountry");
   labelCountryName = $("#labelCountryName");
   fieldCountryName = $("#fieldCountryName");
   fieldCountryCode = $("#fieldCountryCode");

   buttonLoad.click(function(){
       loadCountries();
   });

   dropDownCountry.on("change", function (){
        changeFormStateToSelectedCountry();
   });

   buttonAddCountry.click("click", function (){
       if(buttonAddCountry.val() == "Add"){
           addCountry();
       }else{
           changeFormStateToNew();
       }
   });
   buttonUpdateCountry.click(function() {
       updateCountry();
   });

   buttonDeleteCountry.click(function (){
       deletCountry();
   })
});

function showToastMessage(message){
    $("#toastMessage").text(message);
    $(".toast").toast('show');
}

