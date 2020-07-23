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

async function getGroups() {
    var servletURL = "/answer?assignment-key=" + getUrlVars()['assignment-key'] + "&question-key=" + getUrlVars()["question-key"];

    console.log(servletURL);
    console.log("hi");
    const response = await fetch(servletURL);
    const questions = await response.json(); 
    console.log(questions)   
    // var myDiv = document.getElementById("questions-container");
    // var listHolder = document.createElement('ul');

    // questions.forEach(question => {
    //     console.log(question.name);
    //     var li = document.createElement('LI');
    //     var a = document.createElement('A');
        
    //     var link = document.createTextNode(question.name); 
    //     a.appendChild(link);
    //     a.href = "groups.html?assignment-key=" + getUrlVars()['assignment-key'] + "&question-key=" + question.key;
    //     li.append(a);
    //     listHolder.append(li);
    // });

    // myDiv.appendChild(listHolder);
}

function getUrlVars() {
  var vars = {};
  var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
    vars[key] = value;
  });
  return vars;
}