$(document)
		.ready(
				function() {

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

					$('#btFileFetch')
							.on(
									'click',
									function() {
										var name = document
												.getElementById('dwlFile').value;
										$
												.ajax({
													type : 'GET',
													url : '/fetchFile',
													data : {
														name : name
													},
													success : function(data) {
														replaceList(data, 'dul');
													},
													error : function(data) {
														document
																.getElementById('dul').innerHTML = '<li>'
																+ data
																+ "</li>";
													},
												});
									});

					$('#btFileUpload')
							.on(
									('click'),
									function() {
										var ful = document
												.getElementById('uplFile');
										var formData = new FormData();
										document.getElementById("uploadStatus").innerHTML = ful.files[0].name;
										formData.append("uploadfile",
												ful.files[0]);
										$
												.ajax({
													type : 'POST',
													url : '/uploadFile',
													data : formData,
													enctype : 'multipart/form-data',
													processData : false,
													contentType : false,
													cache : false,
													success : function(data) {
														document
																.getElementById("uploadStatus").innerHTML = 'Success ->'
																+ data;
													},
													error : function(data) {
														document
																.getElementById("uploadStatus").innerHTML = 'Error ->'
																+ data;
													}
												});
									});

				});

function replaceList(data, tId) {
	var list = "";
	for (var i = 0; i < data.length; i++) {
		list += "<li><a href=\"/download?name=" + data[i]
				+ "\">Download</a> => " + data[i] + "</li>";
	}
	document.getElementById('dul').innerHTML = list;
}

function replaceTbody(data, tId) {
	var tableRows = "";
	for (var i = 0; i < data.length; i++) {
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
	$('#' + tId + ' tbody').html(tableRows);
}