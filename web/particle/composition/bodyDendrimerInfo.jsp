<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script language="JavaScript">
<!--

//-->
</script>
<br>
<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="4">
				<div align="justify">
					Composition Properties
				</div>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Branch</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when
						test="${canCreateNanoparticle eq 'true' && isRemote eq 'false'}">
						<html:select property="dendrimer.branch"
							onkeydown="javascript:fnKeyDownHandler(this, event);"
							onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
							onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
							onchange="fnChangeHandler_A(this, event);">
							<option value="">
								--?--
							</option>
							<html:options name="allDendrimerBranches" />
						</html:select>
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.dendrimer.branch}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Repeat Unit</strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when
						test="${canCreateNanoparticle eq 'true' && isRemote eq 'false'}">
						<html:text property="dendrimer.repeatUnit" />
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.dendrimer.repeatUnit}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		<tr>
			<td class="leftLabel">
				<strong>Generation</strong>
			</td>
			<td class="label">
				<c:choose>
					<c:when
						test="${canCreateNanoparticle eq 'true' && isRemote eq 'false'}">
						<html:select property="dendrimer.generation"
							onkeydown="javascript:fnKeyDownHandler(this, event);"
							onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
							onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
							onchange="fnChangeHandler_A(this, event);">
							<option value="">
								--?--
							</option>
							<html:options name="allDendrimerGenerations" />
						</html:select>
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.dendrimer.generation}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
			<td class="label">
				<strong>Molecular Formula</strong>
			</td>
			<td class="rightLabel">
				<c:choose>
					<c:when
						test="${canCreateNanoparticle eq 'true' && isRemote eq 'false'}">
						<html:text property="dendrimer.molecularFormula" />
					</c:when>
					<c:otherwise>
						${nanoparticleCompositionForm.map.dendrimer.molecularFormula}&nbsp;
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
	</tbody>
</table>
<br>
<table class="topBorderOnly" cellspacing="0" cellpadding="3"
	width="100%" align="center" summary="" border="0">
	<tbody>
		<tr class="topBorder">
			<td class="formTitle" colspan="4">
				<div align="justify">
					Surface Group Information
				</div>
			</td>
		</tr>
		<tr>
			<td class="completeLabel" colspan="4">
				<table border="0" width="100%">
					<tr>
						<c:choose>
							<c:when
								test="${canCreateNanoparticle eq 'true' && isRemote eq 'false'}">
								<td valign="bottom" width="120">
									<a href="#"
										onclick="javascript:addComponent(nanoparticleCompositionForm, 'composition', 'addSurfaceGroup')"><span
										class="addLink">Add Surface Group</span>
									</a>
								</td>
							</c:when>
							<c:otherwise>
								<td></td>
							</c:otherwise>
						</c:choose>
						<td>
							<logic:iterate name="nanoparticleCompositionForm"
								property="dendrimer.surfaceGroups" id="surfaceGroup"
								indexId="ind">
								<table class="topBorderOnly" cellspacing="0" cellpadding="3"
									width="100%" align="center" summary="" border="0">
									<tbody>
										<tr>
											<c:choose>
												<c:when test="${canCreateNanoparticle eq 'true'}">
													<td class="formSubTitleNoRight" colspan="3">
														Surface Group #${ind+1}
													</td>
													<td class="formSubTitleNoLeft" align="right">
														<a href="#"
															onclick="javascript:removeComponent(nanoparticleCompositionForm, 'composition', ${ind}, 'removeSurfaceGroup')">
															<img src="images/delete.gif" border="0"
																alt="remove this surface group"> </a>
													</td>
												</c:when>
												<c:otherwise>
													<td class="formSubTitle" colspan="4">
														Surface Group ${ind+1}
													</td>
												</c:otherwise>
											</c:choose>
										</tr>
										<tr>
											<td class="leftLabel">
												<strong>Name* </strong>
											</td>
											<td class="label">
												<c:choose>
													<c:when
														test="${canCreateNanoparticle eq 'true' && isRemote eq 'false'}">
														<html:select
															property="dendrimer.surfaceGroups[${ind}].name"
															onkeydown="javascript:fnKeyDownHandler(this, event);"
															onkeyup="javascript:fnKeyUpHandler_A(this, event); return false;"
															onkeypress="javascript:return fnKeyPressHandler_A(this, event);"
															onchange="fnChangeHandler_A(this, event);">
															<option value="">
																--?--
															</option>
															<html:options name="allDendrimerSurfaceGroupNames" />
														</html:select>
													</c:when>
													<c:otherwise>
						${nanoparticleCompositionForm.map.dendrimer.surfaceGroups[ind].name}&nbsp;
					</c:otherwise>
												</c:choose>
											</td>
											<td class="label">
												<strong>Modifier</strong>
											</td>
											<td class="rightLabel">
												<c:choose>
													<c:when
														test="${canCreateNanoparticle eq 'true' && isRemote eq 'false'}">
														<html:text
															property="dendrimer.surfaceGroups[${ind}].modifier" />
													</c:when>
													<c:otherwise>
						${nanoparticleCompositionForm.map.dendrimer.surfaceGroups[ind].modifier}&nbsp;
					</c:otherwise>
												</c:choose>
											</td>
										</tr>
									</tbody>
								</table>
								<br>
							</logic:iterate>
						</td>
					</tr>
				</table>
			</td>
		</tr>
</table>
