<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%-- JSP TAGS --%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="my" tagdir="/WEB-INF/tags"%>

<%-- CONTENT --%>
<my:page>
    <jsp:attribute name="pageHeaderTitle">Attestation Identity CA Policy Options</jsp:attribute>

    <jsp:body>
        <ul>
            <%-- Endorsement validation --%>
            <div class="aca-input-box">
                <form:form method="POST" modelAttribute="initialData" action="policy/update-ec-validation">
                    <li>Endorsement Credential Validation: ${initialData.enableEcValidation ? 'Enabled' : 'Disabled'}
                        <my:editor id="ecPolicyEditor" label="Edit Settings ">
                            <div class="radio">
                                <label><input id="ecTop" type="radio" name="ecValidate" ${initialData.enableEcValidation ? 'checked' : ''}  value="checked"/> Endorsement Credentials will be validated</label>
                            </div>
                            <div class="radio">
                                <label><input id="ecBot" type="radio" name="ecValidate" ${initialData.enableEcValidation ? '' : 'checked'} value="unchecked"/> Endorsement Credentials will not be validated</label>
                            </div>
                        </my:editor>
                    </li>
                </form:form>
            </div>

            <%-- Platform validation --%>
            <div class="aca-input-box">
                <form:form method="POST" modelAttribute="initialData" action="policy/update-pc-validation">
                    <li>Platform Credential Validation: ${initialData.enablePcCertificateValidation ? 'Enabled' : 'Disabled'}
                        <my:editor id="pcPolicyEditor" label="Edit Settings">
                            <div class="radio">
                                <label><input id="pcTop" type="radio" name="pcValidate" ${initialData.enablePcCertificateValidation ? 'checked' : ''} value="checked"/> Platform Credentials will be validated</label>
                            </div>
                            <div class="radio">
                                <label><input id="pcBot" type="radio" name="pcValidate" ${initialData.enablePcCertificateValidation ? '' : 'checked'}  value="unchecked"/> Platform Credentials will not be validated</label>
                            </div>
                        </my:editor>
                    </li>
                </form:form>
            </div>

            <%-- Platform attribute validation --%>
            <div class="aca-input-box">
                <form:form method="POST" modelAttribute="initialData" action="policy/update-pc-attribute-validation">
                    <ul>
                        <li>Platform Attribute Credential Validation: ${initialData.enablePcCertificateAttributeValidation ? 'Enabled' : 'Disabled'}
                            <my:editor id="pcAttributePolicyEditor" label="Edit Settings">
                                <div class="radio">
                                    <label><input id="pcAttrTop" type="radio" name="pcAttributeValidate" ${initialData.enablePcCertificateAttributeValidation ? 'checked' : ''} value="checked"/> Platform Credential Attributes will be validated</label>
                                </div>
                                <div class="radio">
                                    <label><input id="pcAttrBot" type="radio" name="pcAttributeValidate" ${initialData.enablePcCertificateAttributeValidation ? '' : 'checked'}  value="unchecked"/> Platform Credential Attributes will not be validated</label>
                                </div>
                            </my:editor>
                        </li>
                    </ul>
                </form:form>
            </div>

            <%-- Firmware validation --%>
            <div class="aca-input-box">
                <form:form method="POST" modelAttribute="initialData" action="policy/update-firmware-validation">
                    <li>Firmware Validation: ${initialData.enableFirmwareValidation ? 'Enabled' : 'Disabled'}
                        <my:editor id="firmwarePolicyEditor" label="Edit Settings">
                            <div class="radio">
                                <label><input id="firmwareTop" type="radio" name="fmValidate" ${initialData.enableFirmwareValidation ? 'checked' : ''} value="checked"/> Firmware will be validated</label>
                            </div>
                            <div class="radio">
                                <label><input id="firmwareBot" type="radio" name="fmValidate" ${initialData.enableFirmwareValidation ? '' : 'checked'}  value="unchecked"/> Firmware will not be validated</label>
                            </div>
                        </my:editor>
                    </form:form>
                    <ul>
                        <form:form method="POST" modelAttribute="initialData" action="policy/update-ima-ignore">
                            <li>Ignore IMA PCR Entry: ${initialData.enableIgnoreIma ? 'Enabled' : 'Disabled'}
                                <my:editor id="ignoreImaPolicyEditor" label="Edit Settings">
                                    <div class="radio">
                                        <label><input id="imaTop" type="radio" name="ignoreIma" ${initialData.enableIgnoreIma ? 'checked' : ''} value="checked"/> Ignore IMA enabled</label>
                                    </div>
                                    <div class="radio">
                                        <label><input id="imaBot" type="radio" name="ignoreIma" ${initialData.enableIgnoreIma ? '' : 'checked'}  value="unchecked"/> Ignore IMA disabled</label>
                                    </div>
                                </my:editor>
                            </li>
                        </form:form>
                        <form:form method="POST" modelAttribute="initialData" action="policy/update-tboot-ignore">
                            <li>Ignore TBOOT PCRs Entry: ${initialData.enableIgnoreTboot ? 'Enabled' : 'Disabled'}
                                <my:editor id="ignoreTbootPolicyEditor" label="Edit Settings">
                                    <div class="radio">
                                        <label><input id="tbootTop" type="radio" name="ignoretBoot" ${initialData.enableIgnoreTboot ? 'checked' : ''} value="checked"/> Ignore TBoot enabled</label>
                                    </div>
                                    <div class="radio">
                                        <label><input id="tbootBot" type="radio" name="ignoretBoot" ${initialData.enableIgnoreTboot ? '' : 'checked'}  value="unchecked"/> Ignore TBoot disabled</label>
                                    </div>
                                </my:editor>
                            </li>
                        </form:form>
                    </ul>
                </li>
            </div>
        </ul>
    </jsp:body>
</my:page>
