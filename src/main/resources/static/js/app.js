$(document).ready(function() {


	$('#btIsin').on('click', function() {
		var isin = document.getElementById('inIsin').value;
		$.ajax({
			type : 'GET',
			url : '/isin',
			data : {
				isin : isin,
			},
			success : function(data) {
				replaceTbody(data, 'tbIsin'); 
			},
			error : function(data) {
				alert('Ajax Error');
			}
		});


	});
});

function replaceTbody(data, tId){
	var tableRows = "";
	for(var i = 0; i < data.length; i++) {
			tableRows += "<tr><td>";
		tableRows += data[i].MV_SOURCE_ID;
		tableRows += "</td><td>";
		tableRows += data[i].MV_UPLOAD_ID;
		tableRows += "</td><td>";
		tableRows += data[i].MV_ISIN;
		tableRows += "</td><td>";
		tableRows += data[i].MV_MIC;
		tableRows += "</td><td>";
		tableRows += data[i].MV_AS_OF_DATE;
		tableRows += "</td><td>";
		tableRows += data[i].MV_FIELDNAME;
		tableRows += "</td><td>";
		tableRows += data[i].MV_STRINGVALUE;
		tableRows += "</td><td>";
		tableRows += data[i].MV_TIMESTAMP;
		tableRows += "</td><td>";
		tableRows += data[i].MV_DATA_ORIGIN;
		tableRows += "</td><td>";
		tableRows += data[i].MV_URLSOURCE;
		tableRows += "</td><td>";
		tableRows += data[i].MV_COMMENT;
		tableRows += "</td></tr>";
	}
	$('#'+tId+' tbody').html(tableRows); 
}