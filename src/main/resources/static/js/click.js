function toggleHtmlElementVisibility(elementId)
{
	let element = document.getElementById(elementId);

	if (element.classList.value === '') element.classList.add("invisible");
	else element.classList.remove("invisible");
}