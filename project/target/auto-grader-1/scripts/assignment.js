async function getAssignments() {
    var servletURL = "/assignment";

    const response = await fetch(servletURL);
    const assignments = await response.json();
	const assignmentsList = document.getElementById('assignment-container');
	assignmentsList.innerHTML = '';
	assignments.forEach(assignment => {

		const content = `[${assignment.name}] ${assignment.points}`;
		assignmentsList.appendChild(createListElement(content));
    });
}

/** Creates an <li> element containing text. */
function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerHTML = "- " + text;
  return liElement;
}