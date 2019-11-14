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
        "Content-Type": "application/json",
        // "Access-Control_Allow-Origin": "*"
      },
      body: JSON.stringify({
        username: un,
        password: pwd,
        })
    })
    .then(response => {
        console.log(response.json());
        if(response.status == 201){
            window.location.href = "http://127.0.0.1:5500/API/src/main/webapp/home.html";
        } else {
            alert("Nope. Try Again.");
            document.getElementById("loginForm").reset();
        }
    })
    .then(data => console.log(data))
    .catch(error => {
        console.error(error);
    });
}

function HomePageFunc(){
    event.preventDefault();
    console.log("Inside Home Page Func");

    fetch("http://localhost:8080/ExpenseReimbursement/login",{
        credentials: "include",
        headers:{
            "Content-Type":"application/json"
        }
    })
    .then(response => {
        return response.json();
    })
    .then(data => {
        console.log(data);
        document.getElementById("firstName").innerHTML = data.user_first_name;
    })
    .catch(error => {
        console.error(error);
    });
}