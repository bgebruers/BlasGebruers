function toggleDiv(){
    var x = document.getElementById("masInfo");
    if (x.style.display === "none") {
        x.style.display = "block";
        document.getElementById("infoButton").innerHTML = "Hide Info";
    } else {
        x.style.display = "none";
        document.getElementById("infoButton").innerHTML = "Show Info";
    }
}