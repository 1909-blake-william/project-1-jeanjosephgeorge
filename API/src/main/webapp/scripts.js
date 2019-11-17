//LOGIN PAGE FUNCTION
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
//This section is for building the view
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
        let userrole = data.user_role_id;

        document.getElementById("firstName").innerHTML = data.user_first_name;

        //IF ADMIN, DISPLAY ALL RESULTS
        if(userrole == 2){

            fetch(`http://localhost:8080/ExpenseReimbursement/reimbursements`,{
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
                                        //TABLE TO RENDER
                                        //IF USER IS ADMIN 
                                        //CHECK TO MAKE SURE CREATOR CANNOT APPROVE
                                        //IF STATUS IS ALREADY APPROVED
                                        if(data.user_role_id == 1 || data.username == reimb.reimb_author || reimb.reimb_status == "Approved" || reimb.reimb_status == "Denied"){
                                            return `
                                            <tr style="display: table-row" class="${reimb.reimb_status}">
                                                <th scope="row">${reimb.reimb_id}</th>
                                                <td>${reimb.reimb_author}</td>
                                                <td>$${reimb.reimb_amount}</td>
                                                <td>${reimb.reimb_submitted}</td>
                                                <td>${reimb.reimb_resolved}</td>
                                                <td>${reimb.reimb_resolver}</td>
                                                <td>${reimb.reimb_description}</td>
                                                <td>${reimb.reimb_type}</td>
                                                <td>${reimb.reimb_status}</td>
                                                <td></td>
                                                </tr>
                                            `
                                        } else {
                                            return `
                                            <tr style="display: table-row" class="${reimb.reimb_status}">
                                                <th scope="row">${reimb.reimb_id}</th>
                                                <td>${reimb.reimb_author}</td>
                                                <td>$${reimb.reimb_amount}</td>
                                                <td>${reimb.reimb_submitted}</td>
                                                <td>${reimb.reimb_resolved}</td>
                                                <td>${reimb.reimb_resolver}</td>
                                                <td>${reimb.reimb_description}</td>
                                                <td>${reimb.reimb_type}</td>
                                                <td>${reimb.reimb_status}</td>
                                                <td>
                                                    <div class="btn-group" role="group" aria-label="Table-buttons">
                                                        <button id="${reimb.reimb_id}" onClick="approveRequest(this.id)" type="button" class="btn btn-success">Approve</button>
                                                        <button id="${reimb.reimb_id}" onClick="denyRequest(this.id)" type="button" class="btn btn-danger">Deny</button>
                                                    </div>
                                                </td>
                                                </tr>
                                                
                                            `
                                        }
                                    }).join('')}
                                    `
            })
            .catch(error => {
                console.error(error);
            });
        //}
        
        //IF NOT ADMIN, DISPLAY RESULTS PER USER
        }else {
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
                                        if(data.user_role_id == 1 || data.username == reimb.reimb_author){
                                            return `
                                            <tr style="display: table-row" class="${reimb.reimb_status}">
                                                <th scope="row">${reimb.reimb_id}</th>
                                                <td>${reimb.reimb_author}</td>
                                                <td>$${reimb.reimb_amount}</td>
                                                <td>${reimb.reimb_submitted}</td>
                                                <td>${reimb.reimb_resolved}</td>
                                                <td>${reimb.reimb_resolver}</td>
                                                <td>${reimb.reimb_description}</td>
                                                <td>${reimb.reimb_type}</td>
                                                <td>${reimb.reimb_status}</td>
                                                <td></td>
                                                </tr>
                                                
                                            `
                                        } else {
                                            return `
                                            <tr style="display: table-row" class="${reimb.reimb_status}">
                                                <th scope="row">${reimb.reimb_id}</th>
                                                <td>${reimb.reimb_author}</td>
                                                <td>$${reimb.reimb_amount}</td>
                                                <td>${reimb.reimb_submitted}</td>
                                                <td>${reimb.reimb_resolved}</td>
                                                <td>${reimb.reimb_resolver}</td>
                                                <td>${reimb.reimb_description}</td>
                                                <td>${reimb.reimb_type}</td>
                                                <td>${reimb.reimb_status}</td>
                                                <td>
                                                    <div class="btn-group" role="group" aria-label="Table-buttons">
                                                        <button id="${reimb.reimb_id}" onClick="approveRequest(this.id)" type="button" class="btn btn-success">Approve</button>
                                                        <button id="${reimb.reimb_id}" onClick="denyRequest(this.id)" type="button" class="btn btn-danger">Deny</button>
                                                    </div>
                                                </td>
                                                </tr>
                                                
                                            `
                                        }
                                    }).join('')}
                                    `
            })
            .catch(error => {
                console.error(error);
            });
        }})
    .catch(error => {
        console.error(error);
    });
}



//THIS IS TO ADD SUBMISSIONS
function AddSubmission(){
    event.preventDefault();
    
    //FIRST CALL TO GET SESSION DATA
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

        //SECOND CALL TO POST DATA SPECIFIC TO USER
        fetch("http://localhost:8080/ExpenseReimbursement/api", {
            credentials:"include",
            method: "POST",
            headers: {
            "Content-Type": "application/json",
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





function approveRequest(clickId){
    alert('Approve '+clickId);
    fetch("http://localhost:8080/ExpenseReimbursement/login",{
        credentials: "include",
        headers:{"Content-Type":"application/json"}
    })
    .then(response => {return response.json();})
    .then(data => {
        console.log(data);
        let username = data.username;
    
        //SECOND CALL TO PUT DATA SPECIFIC TO USER
        fetch("http://localhost:8080/ExpenseReimbursement/api", {
            credentials:"include",
            method: "PUT",
            headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            reimb_id: clickId,
            reimb_resolver: username,
            reimb_status: "Approved",
            })
        })
        .then(response => {
            return response.json();
        })
        .then(data => {
            if(data == 1){
                window.location.reload();
            } else {
                console.log("Update went wrong");
                window.location.reload(true);
            }
        }   
            )
        .catch(error => {
            console.error(error);
        });
    })
    .catch(error => {
        console.error(error);
    });
}



function denyRequest(clickId){
    fetch("http://localhost:8080/ExpenseReimbursement/login",{
        credentials: "include",
        headers:{"Content-Type":"application/json"}
    })
    .then(response => {return response.json();})
    .then(data => {
        console.log(data);
        let username = data.username;
    
        //SECOND CALL TO PUT DATA SPECIFIC TO USER
        fetch("http://localhost:8080/ExpenseReimbursement/api", {
            credentials:"include",
            method: "PUT",
            headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({
            reimb_id: clickId,
            reimb_resolver: username,
            reimb_status: "Denied",
            })
        })
        .then(response => {
            console.log(response);
            return response.json();
        })
        .then(data => 
            {   console.log(data)
                if(data == 1){
                    window.location.reload(true);
                } else {
                    console.log("Update went wrong")
                    window.location.reload(true);   
                }}
            )
        .catch(error => {
            console.error(error);
        });
    })
    .catch(error => {
        console.error(error);
    });
}