//LOGIN FUNCTION
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

//HOMEPAGE AUTOLOAD FUNCTION
function HomePageFunc(){
    event.preventDefault();
    console.log("Inside Home Page Func");

    fetch("http://localhost:8080/ExpenseReimbursement/login",{
        credentials: "include",
        headers:{"Content-Type":"application/json"}
    })
    .then(response => {return response.json();})
    .then(data => {
        console.log(data);
        let username = data.username;
        document.getElementById("firstName").innerHTML = data.user_first_name;
    })
    .catch(error => {
        console.error(error);
    });
}

//VIEW ALL SUBMISSIONS PAGE
function ViewSubmissions(){
    event.preventDefault();
    
    //FIRST CALL
    fetch("http://localhost:8080/ExpenseReimbursement/login",{
        credentials: "include",
        headers:{"Content-Type":"application/json"}
    })
    .then(response => {return response.json();})
    .then(data => {
        console.log(data);
        let username = data.username;
        document.getElementById("firstName").innerHTML = data.user_first_name;

        //SECOND CALL to Get Data
            fetch(`http://localhost:8080/ExpenseReimbursement/api/${data.username}`,{
            credentials: "include",
            headers:{
                "Content-Type":"application/json"
            }
        })
        .then(response => {
            return response.json();
        })
        .then(data2 => {
            console.log(data2);
            document.getElementById("tableBody").innerHTML = ` 
                                ${data2.map(function(reimb){
                                    return `
                                    <tr>
                                        <th scope="row">${reimb.reimb_id}</th>
                                        <td>${data.user_first_name} ${data.user_last_name}</td>
                                        <td>${reimb.reimb_amount}</td>
                                        <td>${reimb.reimb_submitted}</td>
                                        <td>${reimb.reimb_resolved}</td>
                                        <td>${reimb.reimb_resolver}</td>
                                        <td>${reimb.reimb_description}</td>
                                        <td>${reimb.reimb_type}</td>
                                        <td>${reimb.reimb_status}</td>
                                        <td>
                                            <div style="" class="btn-group" role="group" aria-label="Table-buttons">
                                                <button type="button" class="btn btn-success">Approve</button>
                                                <button type="button" class="btn btn-danger">Deny</button>
                                            </div>
                                        </td>
                                        </tr>
                                        
                                    `
                                }).join('')}
                                `
        })
        .catch(error => {
            console.error(error);
        });
    })
    .catch(error => {
        console.error(error);
    });


    // event.preventDefault();
    // console.log("Inside ViewAllSubmissions Function");    
    // fetch("http://localhost:8080/ExpenseReimbursement/api/${data.username}",{
    //     credentials: "include",
    //     headers:{
    //         "Content-Type":"application/json"
    //     }
    // })
    // .then(response => {
    //     return response.json();
    // })
    // .then(data => {
    //     console.log(data);
    // })
    // .catch(error => {
    //     console.error(error);
    // });
}


//VIEW ALL SUBMISSIONS PAGE
function AddSubmission(){
    event.preventDefault();
    
    //FIRST CALL
    fetch("http://localhost:8080/ExpenseReimbursement/login",{
        credentials: "include",
        headers:{"Content-Type":"application/json"}
    })
    .then(response => {return response.json();})
    .then(data => {
        console.log(data);
        let username = data.username;
        let reimb_amount = document.getElementById("expenseAmount").value;
        let reimb_description = document.getElementById("expenseDescription").value;
        let reimb_type = document.getElementById("expenseType").value;

        //SECOND CALL to Get Data
        fetch("http://localhost:8080/ExpenseReimbursement/api", {
            credentials:"include",
            method: "POST",
            headers: {
            "Content-Type": "application/json",
            // "Access-Control_Allow-Origin": "*"
          },
          body: JSON.stringify({
            reimb_author: username,
            reimb_amount: reimb_amount,
            reimb_description: reimb_description,
            reimb_type: reimb_type
            })
        })
        .then(response => {
            console.log(response.json());
            if(response.status == 201){
                window.location.href = "http://127.0.0.1:5500/API/src/main/webapp/viewsubmissions.html";
            } else {
                // alert("Nope. Try Again.");
                document.getElementById("expenseForm").reset();
                window.location.href = "http://127.0.0.1:5500/API/src/main/webapp/viewsubmissions.html";

            }
        })
        .then(data => console.log(data))
        .catch(error => {
            console.error(error);
        });
    })
    .catch(error => {
        console.error(error);
    })};