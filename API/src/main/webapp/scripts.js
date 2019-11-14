function LoginFunc(){
    event.preventDefault();
    console.log("Inside");
    let un = document.getElementById('inputUsername').value;
    let pwd = document.getElementById('inputPassword').value;
    console.log(un + ": " + pwd);

    fetch("http://localhost:8080/ExpenseReimbursement/login", {
        credentials:"include",
        method: "POST",
        headers: {
        Accept: "text/plain",
        "Content-Type": "application/json",
        // "Access-Control_Allow-Origin": "*"
      },
      body: JSON.stringify({
        username: un,
        password: pwd,
        })
    })
    .then(response => {
        console.log(response);

        if(response.status == 201){
            window.location.href = "http://localhost:5500/API/src/main/webapp/home.html";
        } else {
            alert("Nope. Try Again.");
            document.getElementById("loginForm").reset();
        }
    })
    .catch(error => {
        console.error(error);
    });



}