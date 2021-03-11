
var ERROR_COLOR = "#f51616";
var PASSWORD_PATTERN = "";
function validateContentSize(element, minSize, maxSize){

  var result = false;
  if(element.value.length <= maxSize && element.value.length >= minSize)
    result = true;

  return result;
}

function validatePassword(){

}



function init(){
  //add event listeners
  document.getElementById("userName").addEventListener("change", function(){
      validateContentSize(document.getElementById("userName"),6, 50);
  });

  document.getElementById("password").addEventListener("change", function(){
    validateContentSize(document.getElementById("password"), 50);
  });

  document.getElementById("confirmPassword").addEventListener("change", function(){
    validateContentSize(document.getElementById("confirmPassword"), 50);
  });

  document.getElementById("firstName").addEventListener("change", function(){
    validateContentSize(document.getElementById("firstName"),1, 50);
  });

  document.getElementById("lsatName").addEventListener("change", function(){
    validateContentSize(document.getElementById("lastName"),1, 50);
  });

  document.getElementById("address1").addEventListener("change", function(){
    validateContentSize(document.getElementById("address1"),1, 100);
  });

  document.getElementById("address2").addEventListener("change", function(){
    validateContentSize(document.getElementById("address2"),0, 100);
  });

  document.getElementById("city").addEventListener("change", function(){
    validateContentSize(document.getElementById("city"), 1,50);
  });

  document.getElementById("state").addEventListener("change", function(){
    validateContentSize(document.getElementById("state"), 1, 50);
  });

  document.getElementById("zipCode").addEventListener("change", function(){
    validateContentSize(document.getElementById("zipCode"), 5,10);
  });

  document.getElementById("phone").addEventListener("change", function(){
    validateContentSize(document.getElementById("phone"), 5,10);
  });

  document.getElementById("email").addEventListener("change", function(){
    validateContentSize(document.getElementById("email"), 5,10);
  });

  document.getElementById("dateofBirth").addEventListener("change", function(){
    validateContentSize(document.getElementById("dateOfBirth"), 5,10);
  });



}



init();
