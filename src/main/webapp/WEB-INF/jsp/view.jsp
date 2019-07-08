<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op"%>

<script type="text/javascript" src="${pageContext.request.contextPath}/js/onlyoffice-integration.js"></script>
<script type="text/javascript" src="${apiUrl}"></script>


<portlet:defineObjects />


<div class="onlyoffice-container">
    <div class="onlyoffice-toolbar">
        <div class="navbar navbar-inverse navbar-static-top" data-extension="${toolbarProperties['extension']}">
            <div class="container-fluid">
                <div class="navbar-header hidden-xs">
                    <c:set var="title"><op:translate key="BACK_TEXT" /></c:set>
                    <a href="${toolbarProperties['closeUrl']}" title="${title}" class="navbar-brand no-ajax-link">
                        <i class="glyphicons glyphicons-arrow-left"></i>
                        <span class="sr-only">${title}</span>
                    </a>
                </div>
                
                <p class="navbar-text hidden-xs">
                    <span>${toolbarProperties['documentTitle']}</span>
                </p>
                
                <ul class="nav navbar-nav navbar-right">
                    <li>
                        <a href="${toolbarProperties['closeUrl']}" class="no-ajax-link">
                            <span><op:translate key="BACK_TEXT" /></span>
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </div>

    <div class="onlyoffice-component">
    	<div id="onlyoffice-placeholder" data-onlyoffice-config='${onlyOfficeConfig}'></div>
    </div>
</div>
