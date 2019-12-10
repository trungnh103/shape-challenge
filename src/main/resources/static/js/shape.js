function createPropertiesTable() {
    var propertiesTab = document.createElement("TABLE");
    propertiesTab.setAttribute("id", "propertiesTab");
    document.getElementById("edit-properties").appendChild(propertiesTab);
    categoryChange();
    loadProperties();
}
function loadProperties() {
    var propertyArr = document.getElementById("properties").value.split(',');
    var properties = document.getElementsByName("property");
     for (i = 0; i < properties.length; i++) {
        properties[i].value = propertyArr[i];
     }
}
function categoryChange() {
    var categories = document.getElementById("category");
    var categoryName = categories.options[categories.selectedIndex].text;
    document.getElementById("categoryName").value = categoryName;
    var propertiesTab = document.getElementById("propertiesTab");
    propertiesTab.innerHTML = "";
    var requirements = document.getElementById("category").value;
    requirementArr = requirements.substring(1, requirements.length - 1).split(', ');
    for (i = 0; i < requirementArr.length; i++) {
        var row = propertiesTab.insertRow(i);
        var cell = row.insertCell(0);
        cell.innerHTML = "<input type='text' name='property' value='' placeholder='"
            + requirementArr[i]+ "' onfocusout='updateProperties()'/>";
        }
}

function updateProperties() {
    var properties = document.getElementsByName("property");
    document.getElementById("properties").value = "";
    for (i = 0; i < properties.length; i++) {
  	    if (i != 0) document.getElementById("properties").value += ", ";
  	    document.getElementById("properties").value += properties[i].value;
    }
}