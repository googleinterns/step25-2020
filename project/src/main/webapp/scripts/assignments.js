async function getAssignments() {
    var servletURL = "/assignment";
    const response = await fetch(servletURL);
    const assignments = await response.json();
	//const assignmentsList = document.getElementById('assignments-container');
    
    var myTableDiv = document.getElementById("assignments-container");

    var table = document.createElement('TABLE');

    var tableBody = document.createElement('TBODY');
    var headerTr = document.createElement('TR');
    var nameHeader = document.createElement('TH');
    var statusHeader = document.createElement('TH');
    var pointsHeader = document.createElement('TH');
    nameHeader.innerHTML = "NAME";
    statusHeader.innerHTML = "STATUS";
    pointsHeader.innerHTML = "POINTS";

    headerTr.appendChild(nameHeader);
    headerTr.appendChild(statusHeader);
    headerTr.appendChild(pointsHeader);
    table.appendChild(headerTr);
    table.appendChild(tableBody);

    assignments.forEach(assignment => {
        var tr = document.createElement('TR');
        tableBody.appendChild(tr);
        var td1 = document.createElement('TD');
        td1.innerHTML = assignment.name;
        var td2 = document.createElement('TD');
        td2.innerHTML = assignment.status;
        var td3 = document.createElement('TD');
        td3.innerHTML = assignment.points;
        tr.appendChild(td1);
        tr.appendChild(td2);
        tr.appendChild(td3);
    });
    myTableDiv.appendChild(table);

	//assignmentsList.innerHTML = '';
	//assignments.forEach(assignment => {

//		const content = `[${assignment.name}]`;
//		assignmentsList.appendChild(createListElement(content));

}

/** Creates an <li> element containing text. */
function createListElement(text) {
  const liElement = document.createElement('tr');
  const name = document.createElement('td');
  const status = document.createElement('td');
  liElement.innerHTML = "- " + text;
  return liElement;
}