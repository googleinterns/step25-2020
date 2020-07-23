async function getQuestions() {
    var servletURL = "/question?assignment-key=" + getUrlVars()['assignment-key'];
    const response = await fetch(servletURL);
    const questions = await response.json();    
    var myDiv = document.getElementById("questions-container");
    var listHolder = document.createElement('ul');

    questions.forEach(question => {
        var li = document.createElement('LI');
        var a = document.createElement('A');
        
        var link = document.createTextNode(question.name); 
        a.appendChild(link);
        a.href = "groups.html?assignment-key=" + getUrlVars()['assignment-key'] + "&question-key=" + question.key;
        li.append(a);
        listHolder.append(li);
    });

    myDiv.appendChild(listHolder);
}

function getUrlVars() {
  var vars = {};
  var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
    vars[key] = value;
  });
  return vars;
}