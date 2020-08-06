async function addAnswers() {
    var servletURL = "/addAnswers?assignment-key=" + getUrlVars()['assignment-key'] + "&question-key=" + getUrlVars()["question-key"];
    const response = await fetch(servletURL);
    const answers = await response.json(); 
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