<%@ page language="java" pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<meta http-equiv="expires" content="0" />
		<link rel="shortcut icon" type="image/x-icon" href="images/logo.ico" />
		<link rel="icon" type="image/x-icon" href="images/logo.ico" />
		<link rel="stylesheet" type="text/css"
			href="<%=request.getContextPath()%>/ext/css/ext-all.css" />
		<link rel="stylesheet" type="text/css"
			href="<%=request.getContextPath()%>/css/mainright.css" />
		<link rel="stylesheet" type="text/css"
			href="<%=request.getContextPath()%>/css/base.css" />
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/ext/ext-base.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/ext/ext-all.js"></script>

		<script type="text/javascript"
			src="<%=request.getContextPath()%>/ext/ux/miframe-min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/ext/commonext.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/ext/ext-lang-zh_CN.js"></script>
		<script type="text/javascript" src="../jslib/pagebus/pagebus.js"></script>
		<script type="text/javascript" src="main.js"></script>
		<title>������Ϣ��</title>
	</head>

	<body id="mainmask" onload="init();">
		<input type="hidden" name="ContextPath" id="ContextPath"
			value='<%=request.getContextPath()%>' />
		<div id="north-div">
			<div id="top">
				<!--  <div id="logon" style="float:left;"><img src="images/logo_company.png" height="57" border="0"  /></div>-->
				<ul>
					<li>
						<a href="javascript:logout();"><img src="../images/main/zhu.gif"
								width="13" height="14" border="0" />ע��</a>
					</li>
					<li>
						<span>|</span>
					</li>
					<li>
						<a href="javascript:modefiyuserpwd();"><img
								src="../images/main/xg.gif" width="13" height="14" border="0" />�޸�����</a>
					</li>
				</ul>
			</div>
			<div class="clear"></div>
		</div>

		<div id="west-div">
			<div id="menudiv">
			</div>
		</div>
		<div id="window-win-pwd" title="�޸�����" class="x-hidden" width="320"
			height="160">
				<div class="x-window-body">
					<form id="pwdeditform">
						<table cellspacing="0" class="table-width">
							<tr>
								<td style="height: 0px; width: 30%;"></td>
								<td style="height: 0px; width: 70%;"></td>
							</tr>
							<tr>
								<td class="t">
									�����룺
									<span class="bitian">*</span>
								</td>
								<td class="ts">
									<input type="password" id="oldpwd" name="oldpwd" />
								</td>
							</tr>
							<tr>
								<td class="t">
									�����룺
									<span class="bitian">*</span>
								</td>
								<td class="ts">
									<input type="password" id="newpwd" name="newpwd" />
								</td>

							</tr>
							<tr>
								<td class="t">
									ȷ�����룺
									<span class="bitian">*</span>
								</td>
								<td class="ts">
									<input type="password" id="againpwd" name="againpwd" />
								</td>

							</tr>

						</table>
					</form>
			</div>
		</div>
	</body>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/js/yav/yav.js"></script>
	<script type="text/javascript"
		src="<%=request.getContextPath()%>/js/yav/yav-config.js"></SCRIPT>
</html>