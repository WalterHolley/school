
var regex = null;
var requiredFieldIDs = ["userName", "password", "confirmPassword", "firstName",
"lastName", "dateOfBirth", "address1", "city", "state", "zipCode", "phone","email"];

var optionalFieldIDs = ["address2"];


function resetElements(){
  //reeset required fields
  var i;
  gender = document.getElementsByName("gender");
  maritalStatus = document.getElementsByName("maritalStatus");


  //clear required fields
  for(i = 0; i < requiredFieldIDs.length; i++){
    element = document.getElementById(requiredFieldIDs[i]);
    element.value = '';
    element.classList.remove("is-valid");
    element.classList.remove("is-invalid");
  }

  //clear gender radio button
  for(i = 0; i < gender.length; i++){
    gender[i].checked = false;
  }

  //clear marital status radio button
  for(i = 0; i < maritalStatus.length; i++){
    maritalStatus[i].checked = false;
  }

}


function validateZipCode(element){
  result = false;
  let zipCodeRegex = /(^\d{5}-\d{4})|(^\d{9})|(^\d{5})/;
  let zipCodeRegexNineDigit = /(^\d{9})/;

  length = element.value.length;
  regex = new RegExp(zipCodeRegex);

  if(regex.test(element.value)){

    if(length == 10 || length == 5){
      result = true;
    }else if(length == 9 && zipCodeRegexNineDigit.test(element.value)){
      result = true;
      element.value = element.value.slice(0,5) + "-" + element.value.slice(5,9);
    }

  }

  return result;
}


function showElement(elementName){
  document.getElementById(elementName).classList.remove("hide");

}

function hideElement(elementName){
  document.getElementById(elementName).classList.add("hide");

}

function markValidElement(element){
  //remove invalid class
  element.classList.remove("is-invalid");

  //add valid class
  element.classList.add("is-valid");
}

function markInvalidElement(element){
  //remove valid class
  element.classList.remove("is-valid");

  //remove invalid class
  element.classList.add("is-invalid");

}


//Submit form
async function sendForm(){
  var form = document.getElementById("registrationForm");
  var data = new FormData(form);

  let response = await fetch('./validateForm.php',{
    method: 'POST',
    body: data
  });

  let result = await response.json();

  if(result.status == "ERROR"){

  }
  else{
    window.location.replace("./confirmation.php?id=" + result.id);
  }

}
