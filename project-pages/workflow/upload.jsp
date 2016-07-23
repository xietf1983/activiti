<%@ page language="java" contentType="text/html; charset=GB18030"
	pageEncoding="GB18030"%>
<%
	org.apache.commons.fileupload.disk.DiskFileItemFactory factory = new org.apache.commons.fileupload.disk.DiskFileItemFactory();
	// Create a new file upload handler
	org.apache.commons.fileupload.servlet.ServletFileUpload upload = new org.apache.commons.fileupload.servlet.ServletFileUpload(factory);
	String possess = "";

	java.util.List items = upload.parseRequest(request);
	System.out.print(items.size());
	java.util.Iterator iterator = items.iterator();
	while (iterator.hasNext()) {
		org.apache.commons.fileupload.FileItem item = (org.apache.commons.fileupload.FileItem) iterator.next();
		if (item.isFormField()) {
			String fieldName = item.getFieldName();
			String value = item.getString();
			if (fieldName.equals("possess")) {
				possess = value;
			}
		}
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
		<title>Insert title here</title>
	</head>
	<body>


	</body>
</html>