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
}

async function getAnswers() {
    var servletURL = "/answer?assignment-key=" + getUrlVars()['assignment-key'] + "&question-key=" + getUrlVars()["question-key"];

    const response = await fetch(servletURL);
    const answers = await response.json(); 
    // show them on the page

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