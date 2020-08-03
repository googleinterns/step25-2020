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

    var myDiv = document.getElementById("group-container");

    let groups = new Map();

    //create group => answers hash
    answers.forEach(answer => {
        var filePath = "../" + answer.filePath;
        var groupKey = answer.groupKey;
        
        var image = document.createElement("img");
        image.src = "../" + filePath;
        image.setAttribute("overflow", "hidden");
        image.setAttribute("object-fit", "cover");
        image.setAttribute("height", "500");
        image.setAttribute("width", "500");
        image.setAttribute("alt", "answer");

        if (groups.has(groupKey) == false) {
            groups.set(groupKey, []);
        }
        var prev = groups.get(groupKey);
        prev.push(image);
        groups.set(groupKey, prev);
        
    });

    //for each group, create new div and add appropriate objects into it
    for (let [key, values] of groups) {
        var group = document.createElement("div"); 
        group.setAttribute("border", "thick solid #0000FF");
        for (value of values) {
            group.appendChild(value);
        }
        myDiv.appendChild(group);
    }
}

async function computerGenerateGroups() {
    var servletURL = "/group?assignment-key=" + getUrlVars()['assignment-key'] + "&question-key=" + getUrlVars()["question-key"];
    const response = await fetch(servletURL);
    const answers = await response.json();  
    console.log(answers);
}

async function getGroupImage() {
    // get image using groupKey
    var servletURL = "/getImage?groupKey=" + getUrlVars()['groupKey'];
    const response = await fetch(servletURL);
    const imgFilePath = await response.json();  
    const filePath = imgFilePath[0];

    //add image to div
    var imageDiv = document.getElementById("imgDiv");
    var image = document.createElement("img");
    image.src = "../" + filePath;
    image.setAttribute("overflow", "hidden");
    image.setAttribute("object-fit", "cover");
    image.setAttribute("height", "500");
    image.setAttribute("width", "500");
    image.setAttribute("alt", "answer");
}


async function goToNextPage() {
    var servletURL = "/getUngradedGroupKeys" + getUrlVars()['assignment-key'] + "&question-key=" + getUrlVars()["question-key"];
    const response = await fetch(servletURL);
    const json = await response.json();
    if (len(json) > 0) {
        window.location = "/pages/grading.html" + getUrlVars()['assignment-key'] + "&question-key=" + getUrlVars()["question-key"] + "&groupKey=" + json[0];
    }
    else {
        window.location = "/pages/groups.html";
    }

}

function getUrlVars() {
  var vars = {};
  var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
    vars[key] = value;
  });
  return vars;
}


