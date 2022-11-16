<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
	 
	 	<!-- Common imports in pages -->
	 	<jsp:include page="../header.jsp" />
	   
	   <title>Sicuro di voler disabilitare questi satelliti?</title>
	 </head>
	 
	<body class="d-flex flex-column h-100">
	 
		<!-- Fixed navbar -->
		<jsp:include page="../navbar.jsp"></jsp:include>
	 
	
		<!-- Begin page content -->
		<main class="flex-shrink-0">
		  <div class="container">
		  
		  		<div class="alert alert-success alert-dismissible fade show  ${successMessage==null?'d-none':'' }" role="alert">
				  ${successMessage}
				  <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close" ></button>
				</div>
				<div class="alert alert-danger alert-dismissible fade show ${errorMessage==null?'d-none':'' }" role="alert">
				 ${errorMessage}
				  <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close" ></button>
				</div>
				<div class="alert alert-info alert-dismissible fade show d-none" role="alert">
				  Aggiungere d-none nelle class per non far apparire
				   <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close" ></button>
				</div>
		  
		  
		  
		  		<div class='card'>
				    <div class='card-header'>
				        <h3 class="text-danger">Sicuro di voler disabilitare questi satelliti?</h3> 
				    </div>
				    <div class='card-body'>
				    	<div>
				    		<p>Numero Voci totali presenti nel sistema: ${sizeall }</p>
				    		<p> Numero Voci che verranno modificate in seguito alla procedura: ${size }</p>
				    	</div>
				    
				        <div class='table-responsive'>
				            <table class='table table-striped ' >
				                <thead>
				                    <tr>
			                         	<th>Denominazione</th>
				                        <th>Codice</th>
				                        <th>Data di Lancio</th>
				                        <th>Data di Rientro</th>
				                        <th>Stato</th>
				                    </tr>
				                </thead>
				                <tbody>
				                	<c:forEach items="${disableall_satellite_attr }" var="satelliteItem">
										<tr>
											<td>${satelliteItem.denominazione }</td>
											<td>${satelliteItem.codice }</td>
											<td><fmt:formatDate type = "date" value = "${satelliteItem.dataLancio }" /></td>
											<td><fmt:formatDate type = "date" value = "${satelliteItem.dataRientro }" /></td>
											<td>${satelliteItem.stato }</td>
										</tr>
									</c:forEach>
				                </tbody>
				            </table>
				        </div>
				        
				        <div class="col-12">
							<a href="${pageContext.request.contextPath}/satellite/search" class='btn btn-outline-secondary' style='width:80px'>
					            <i class='fa fa-chevron-left'></i> Back 
					        </a>
					       
							<form:form method="post" action="disabilita" class="row g-3 ">
					        <div class="col-12">
								<button type="submit" name="submit" value="submit" id="submit" class="btn btn-danger">Disabilita</button>
							</div>
							</form:form>
						</div>
				   
					<!-- end card-body -->			   
			    </div>
			<!-- end card -->
			</div>	
		 
		   
		 <!-- end container -->  
		  </div>
		  
		</main>
		
		<!-- Footer -->
		<jsp:include page="../footer.jsp" />
		
	</body>
</html>