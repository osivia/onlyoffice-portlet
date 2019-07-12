<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op"%>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/onlyoffice-integration.js"></script>
<script type="text/javascript" src="${apiUrl}"></script>


<portlet:defineObjects />


<div class="onlyoffice-container">
    <div class="onlyoffice-component">
    	<div id="onlyoffice-placeholder" data-onlyoffice-config='${onlyOfficeConfig}'></div>
    </div>
</div>
