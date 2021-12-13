$('.form-signin').on('click', '.btn', function(event){
  console.log("Shortening URL...");
  event.preventDefault()
  $.ajax({
    url : '/create',
    type : 'POST',
    contentType: 'application/json; charset=utf-8',
    data : JSON.stringify({
              "url" : $('#url').val(),
              "choice" : $('#choice').val()
            }),
    success : function(response){
      var element = `<div class="alert alert-success" role="alert">Shortened URL: `+ `/link/` + response +`</div>`;
      $('.form-signin').append(element);
    },
    error : function(err) {
      err = err.responseJSON;
      var element = `<div class="alert alert-danger" role="alert">Error: `+ err['message'] + `</div>`;
      $('.form-signin').append(element);
    }
  });
});