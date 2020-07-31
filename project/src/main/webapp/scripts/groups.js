// https://www.w3schools.com/html/html5_draganddrop.asp
function allowDrop(ev) {
  ev.preventDefault();
}

function drag(ev) {
  ev.dataTransfer.setData("text", ev.target.id);
}

function drop(ev) {
  ev.preventDefault();
  var data = ev.dataTransfer.getData("text");
  ev.target.appendChild(document.getElementById(data));
}

async function addAnswers() {
    var servletURL = "/addAnswers?assignment-key=" + getUrlVars()['assignment-key'] + "&question-key=" + getUrlVars()["question-key"];
    const response = await fetch(servletURL);
    const answers = await response.json(); 
    console.log(answers)
}

async function getAnswers() {
    var servletURL = "/answer?assignment-key=" + getUrlVars()['assignment-key'] + "&question-key=" + getUrlVars()["question-key"];

    const response = await fetch(servletURL);
    const answers = await response.json(); 
    // show them on the page

    var myDiv = document.getElementById("group-container");
    answers.forEach(answer => {
        var filePath = "../" + answer.filePath;
        console.log(filePath);
        var image = document.createElement("img");
        image.src = "../" + filePath;
        image.setAttribute("height", "500");
        image.setAttribute("width", "500");
        image.setAttribute("alt", "answer");
        myDiv.appendChild(image);
    });

}

async function computerGenerateGroups() {
    var servletURL = "/group?assignment-key=" + getUrlVars()['assignment-key'] + "&question-key=" + getUrlVars()["question-key"];
    const response = await fetch(servletURL);
    const answers = await response.json();  
    console.log(answers);

}

function getUrlVars() {
  var vars = {};
  var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
    vars[key] = value;
  });
  return vars;
}