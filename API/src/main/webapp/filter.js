function filterSelection(val){
    //Turns Everything on if selection is ALL
    if(val == 'All'){
        let alls = document.getElementsByTagName('tr')
        for(let a = 0; a<alls.length; a++){
        alls[a].style.display = 'table-row';
        }
    } else {

        //Onclick, first turns all off
        let all = document.getElementsByTagName('tr')
        for(let a = 0; a<all.length; a++){
            if(all[a].id == 'mainRow'){
            } else {
                all[a].style.display = 'none';
            }
    }
    let rows = document.getElementsByClassName(val);
    console.log(rows);

        //Then turns your selection On
        for(let i = 0; i<rows.length; i++){
            if (rows[i].style.display == 'table-row'){
                rows[i].style.display = 'none';
                } else {
                    rows[i].style.display = 'table-row';
                }
        }
    }
}

