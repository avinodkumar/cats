/**
 * Copyright 2014 Comcast Cable Communications Management, LLC
 *
 * This file is part of CATS.
 *
 * CATS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CATS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CATS.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.comcast.cats;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationActionMap;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;

import com.comcast.cats.info.SnmpServiceReturnMesage;
import com.comcast.cats.service.SnmpService;
import com.comcast.cats.service.WebServiceReturnEnum;

/**
 * Class implements the actions in the SnmpForm. It is performing V1,V2 & V3 versions of SNMP get and set operations.
 * It uses remote ejb lookup for performing the request.
 * @author TATA
 *
 */
public class SnmpFormController implements ItemListener, DocumentListener
{
    // The invalid input parameter messages
    static final String INVALID_PORT_MESSAGE = "Please Enter valid Port Number";
    static final String INVALID_PWD_MESSAGE = "The password should contain atleast 8 characters";
    static final String INVALID_IP_MESSAGE = "Please enter a valid IP Address";
    static final String INVALID_OID_MESSAGE = "Please enter a valid OID";
    static final String LOOKUP_FAILED_MESSAGE = "Failed to locate the remote bean";
    static final String REQUEST_TIMEDOUT_MESSAGE = "Request timed out";

    /**
     * Variable to hold instance of ApplicationActionMap, which contains each entry corresponds to
     * an @Action method in SnmpFormController class.
     */
    private final ApplicationActionMap actionMap;
    /**
     * Variable to hold SnmpForm instance.
     */
    private final SnmpForm snmpForm;

    // variables for making sure whether all the mandatory fields are filled.
    private boolean oidEntered;
    private boolean ipEntered;
    private boolean userNameEntered;
    private boolean authPassEntered;
    private boolean privacyPassEntered;
    private boolean setValueEntered;

    /**
     * The variable that holds the SnmpService lookup instance.
     */
    private SnmpService snmpService;
    /**
     * The constructor which takes SnmpForm as parameter.
     * It creates the ApplicationActionMap which contains the @Action methods.
     * @param form SnmpForm instance
     */
    public SnmpFormController(final SnmpForm form)
    {
        snmpForm = form;

        //
        final ApplicationContext applicationContext = Application.getInstance().getContext();
        final ResourceMap resource = applicationContext.getResourceMap(SnmpFormController.class);
        resource.injectComponent(snmpForm);
        actionMap = applicationContext.getActionMap(this);
    }

    /**
     * Getter method for ApplicationActionMap of SnmpFormController class.
     * @return ApplicationActionMap
     */
    ApplicationActionMap getActionMap()
    {
        return actionMap;
    }

    /**
     * This method obtains the SnmpService using the WSDL url.
     * @param url
     *          The url for the WSDL location.
     */
    private void connectToWsdl(final String wsdlUrl)
    {
    	try {
			SnmpClient snmpClient = new SnmpClient(new URL(wsdlUrl));
			snmpService = snmpClient.getProxy();
		} catch (MalformedURLException e) {
			snmpService = null;
			e.printStackTrace();
		}
    }

    /**
     * Handler for ItemListener. Invoked when the snmp version is changed. It
     * will show necessary input fields according to the version selected. Also
     * check for the mandatory fields, for enabling or disabling 'Get' and 'Set'
     * buttons.
     * @param itemEvent This is generated when an item selection changes.
     */
    @Override
    public void itemStateChanged(final ItemEvent itemEvent)
    {
        snmpForm.cardLayout.show(snmpForm.optionContainer, (String)itemEvent.getItem());
        if (SnmpForm.V3.equals(snmpForm.getVersionCombo().getSelectedItem()))
        {
            if (ipEntered && oidEntered && userNameEntered
                    && authPassEntered && privacyPassEntered)
            {
                snmpForm.getButton.setEnabled(true);
                if (setValueEntered)
                {
                    snmpForm.setButton.setEnabled(true);
                }
                else
                {
                    snmpForm.setButton.setEnabled(false);
                    setValueEntered = false;
                }
            }
            else
            {
                snmpForm.getButton.setEnabled(false);
                snmpForm.setButton.setEnabled(false);
            }
        }
        else
        {
            if (ipEntered && oidEntered)
            {
                snmpForm.getButton.setEnabled(true);
                if (setValueEntered)
                {
                    snmpForm.setButton.setEnabled(true);
                }
                else
                {
                    snmpForm.setButton.setEnabled(false);
                    setValueEntered = false;
                }
            }
            else
            {
                snmpForm.getButton.setEnabled(false);
                snmpForm.setButton.setEnabled(false);
            }
        }
    }

    /**
     * The handler for the DocumentListener. It gives notification that a
     * portion of the document has been removed.
     * @param documentEvent Interface for document change notification.
     */
    @Override
    public void changedUpdate(final DocumentEvent documentEvent)
    {
    }

    /**
     * The handler for the DocumentListener. It gives notification that there
     * was an insert into the document. This is keeping track of which all
     * inputs are entered, for enabling Set and Get buttons. TargetIP and OID
     * are mandatory for Snmp V1, V2, where as V3 requires 3 more mandatory
     * fields UserName, AuthenticationPassWord and PrivacyPassWord. The 'Set' is
     * enabled when the value to be set is entered, with all the mandatory
     * fields.
     * @param documentEvent Interface for document change notification.
     */
    @Override
    public void insertUpdate(final DocumentEvent documentEvent)
    {
        if (documentEvent.getDocument().equals(snmpForm.setValueDoc))
        {
            setValueEntered = true;
        }
        else if (documentEvent.getDocument().equals(snmpForm.targetIpDoc))
        {
            ipEntered = true;
        }
        else if (documentEvent.getDocument().equals(snmpForm.oidDoc))
        {
            oidEntered = true;
        }

        else if (documentEvent.getDocument().equals(snmpForm.userNameDoc))
        {
            userNameEntered = true;
        }

        else if (documentEvent.getDocument().equals(snmpForm.authPassWordDoc))
        {
            authPassEntered = true;
        }

        else if (documentEvent.getDocument().equals(snmpForm.privacyPassWordDoc))
        {
            privacyPassEntered = true;
        }

        if (SnmpForm.V3.equals(snmpForm.getVersionCombo().getSelectedItem()))
        {
            if (ipEntered && oidEntered && userNameEntered
                    && authPassEntered && privacyPassEntered)
            {
                snmpForm.getButton.setEnabled(true);
                if (setValueEntered)
                {
                    snmpForm.setButton.setEnabled(true);
                }
            }
        }
        else
        {
            if (ipEntered && oidEntered)
            {
                snmpForm.getButton.setEnabled(true);
                if (setValueEntered)
                {
                    snmpForm.setButton.setEnabled(true);
                }
            }
        }
    }

    /**
     * The handler for the DocumentListener. It gives notification that a
     * portion of the document has been removed. This is keeping track of which
     * all inputs are entered, for disabling Set and Get buttons. If any of the
     * mandatory field is empty,the Get and Set buttons will be disabled.
     * @param documentEvent Interface for document change notification.
     */
    @Override
    public void removeUpdate(final DocumentEvent documentEvent)
    {
        if (documentEvent.getDocument().equals(snmpForm.setValueDoc))
        {
            if (null == snmpForm.getSetValueTextFld().getText() || snmpForm.getSetValueTextFld().getText().isEmpty())
            {
                snmpForm.setButton.setEnabled(false);
                setValueEntered = false;
            }
        }
        else if (documentEvent.getDocument().equals(snmpForm.targetIpDoc))
        {
            if (null == snmpForm.getTargetIpTextFld().getText() || snmpForm.getTargetIpTextFld().getText().isEmpty())
            {
                ipEntered = false;
            }
        }
        else if (documentEvent.getDocument().equals(snmpForm.oidDoc))
        {
            if (null == snmpForm.getOidTextFld().getText() || snmpForm.getOidTextFld().getText().isEmpty())
            {
                oidEntered = false;
            }
        }
        else if (documentEvent.getDocument().equals(snmpForm.userNameDoc))
        {
            if (null == snmpForm.getUserNameTextFld().getText() || snmpForm.getUserNameTextFld().getText().isEmpty())
            {
                userNameEntered = false;
            }
        }
        else if (documentEvent.getDocument().equals(snmpForm.authPassWordDoc))
        {
            final String authPassWord = String.valueOf(snmpForm.getAuthPassTextFld().getPassword());
            if (null == authPassWord || authPassWord.isEmpty())
            {
                authPassEntered = false;
            }
        }
        else if (documentEvent.getDocument().equals(snmpForm.privacyPassWordDoc))
        {
            final String privPassWord = String.valueOf(snmpForm.getPrivPassTextFld().getPassword());
            if (null == privPassWord || privPassWord.isEmpty())
            {
                privacyPassEntered = false;
            }
        }

        if (SnmpForm.V3.equals(snmpForm.getVersionCombo().getSelectedItem()))
        {
            if (!ipEntered || !oidEntered || !userNameEntered
                    || !authPassEntered || !privacyPassEntered)
            {
                snmpForm.getButton.setEnabled(false);
                snmpForm.setButton.setEnabled(false);
            }
        }
        else
        {
            if (!ipEntered || !oidEntered)
            {
                snmpForm.getButton.setEnabled(false);
                snmpForm.setButton.setEnabled(false);
            }
        }
    }

    /**
     * The action for 'Get' button.
     * It checks whether the input parameters are valid and performs the set operation.
     */
    @Action
    public void getAction()
    {
        final String versionSelected = snmpForm.getVersionCombo().getSelectedItem().toString();
        final String targetIP = snmpForm.getTargetIpTextFld().getText().trim();
        final String oid = snmpForm.getOidTextFld().getText().trim();
        final String portNumber = snmpForm.getPortTextFld().getText().trim();
        int port = SnmpManager.DEFAULT_PORT_NUMBER;

        String authenticationPassWord = null;
        String privacyPassWord = null;
        String userName = null;
        String communityName = null;

        if (!SnmpUtil.isValidIp(targetIP))
        {
            JOptionPane.showMessageDialog(null,  INVALID_IP_MESSAGE);
            return;
        }
        if (!SnmpUtil.isValidOid(oid))
        {
            JOptionPane.showMessageDialog(null, INVALID_OID_MESSAGE);
            return;
        }
        if (null != portNumber && !portNumber.isEmpty())
        {
            try
            {
                port = Integer.parseInt(portNumber);
            }
            catch (NumberFormatException e)
            {
                JOptionPane.showMessageDialog(null, INVALID_PORT_MESSAGE);
                return;
            }
        }

        if (SnmpForm.V3.equals(versionSelected))
        {
            authenticationPassWord = String.valueOf(snmpForm.getAuthPassTextFld().getPassword());
            privacyPassWord = String.valueOf(snmpForm.getPrivPassTextFld().getPassword());
            userName = snmpForm.getUserNameTextFld().getText().trim();
            if (!SnmpUtil.isValidPassword(authenticationPassWord) || !SnmpUtil.isValidPassword(privacyPassWord))
            {
                JOptionPane.showMessageDialog(null, INVALID_PWD_MESSAGE);
                return;
            }
        }
        else if (SnmpForm.V1.equals(versionSelected))
        {
            communityName = String.valueOf(snmpForm.getCommunityTextFldV1().getPassword());
        }
        else if (SnmpForm.V2.equals(versionSelected))
        {
            communityName = String.valueOf(snmpForm.getCommunityTextFldV2().getPassword());
        }

        connectToWsdl(snmpForm.getWSDLLocationTextFld().getText().trim());
        SnmpServiceReturnMesage snmpServiceReturnMessage = null;
        if (null != snmpService)
        {
            snmpServiceReturnMessage = snmpService.get(oid,
                    communityName, targetIP, port, userName, authenticationPassWord, privacyPassWord);
        }
        showResult(snmpServiceReturnMessage);
    }

    /**
     * The action for 'Set' button.
     * It checks whether the input parameters are valid and performs the set operation.
     */
    @Action
    public void setAction()
    {
        final String versionSelected =  snmpForm.getVersionCombo().getSelectedItem().toString();
        final String targetIP =  snmpForm.getTargetIpTextFld().getText().trim();
        final String oid =  snmpForm.getOidTextFld().getText().trim();
        final String portNumber =  snmpForm.getPortTextFld().getText().trim();
        int port = SnmpManager.DEFAULT_PORT_NUMBER;

        String authenticationPassWord = null;
        String privacyPassWord = null;
        String userName = null;
        String communityName = null;

        if (!SnmpUtil.isValidIp(targetIP))
        {
            JOptionPane.showMessageDialog(null, INVALID_IP_MESSAGE);
            return;
        }
        if (!SnmpUtil.isValidOid(oid))
        {
            JOptionPane.showMessageDialog(null, INVALID_OID_MESSAGE);
            return;
        }
        if (null != portNumber && !portNumber.isEmpty())
        {
            try
            {
                port = Integer.parseInt(portNumber);
            }
            catch (NumberFormatException e)
            {
                JOptionPane.showMessageDialog(null, INVALID_PORT_MESSAGE);
                return;
            }
        }

        if (SnmpForm.V3.equals(versionSelected))
        {
            authenticationPassWord = String.valueOf(snmpForm.getAuthPassTextFld().getPassword());
            privacyPassWord = String.valueOf(snmpForm.getPrivPassTextFld().getPassword());
            userName = snmpForm.getUserNameTextFld().getText().trim();
            if (!SnmpUtil.isValidPassword(authenticationPassWord) || !SnmpUtil.isValidPassword(privacyPassWord))
            {
                JOptionPane.showMessageDialog(null, INVALID_PWD_MESSAGE);
                return;
            }
        }
        else if (SnmpForm.V1.equals(versionSelected))
        {
            communityName = String.valueOf(snmpForm.getCommunityTextFldV1().getPassword());
        }
        else if (SnmpForm.V2.equals(versionSelected))
        {
            communityName = String.valueOf(snmpForm.getCommunityTextFldV2().getPassword());
        }

        connectToWsdl(snmpForm.getWSDLLocationTextFld().getText().trim());
        SnmpServiceReturnMesage snmpServiceReturnMessage = null;
        if (null != snmpService)
        {
            final String setValue = snmpForm.getSetValueTextFld().getText();
            final String setValueType = snmpForm.getSetValueTypeCombo().getSelectedItem().toString();
            snmpServiceReturnMessage = snmpService.set(oid,
                    communityName, targetIP, port, setValue, setValueType, userName, authenticationPassWord,
                    privacyPassWord);
        }
        showResult(snmpServiceReturnMessage);
    }

    /**
     * The action for the clear button.
     */
    @Action
    public void clearAction()
    {
        snmpForm.getResultTextFld().setText("");
    }

    /**
     * Helper method for showing the result in the result TextArea. It shows the
     * value if the result is SUCCESS else shows the Snmp failure ServiceCode.
     * @param snmpServiceReturnMessage The result of Snmp Get/Set operation
     */
    private void showResult(final SnmpServiceReturnMesage snmpServiceReturnMessage)
    {
        String result = REQUEST_TIMEDOUT_MESSAGE;

        if (null == snmpService)
        {
            result = LOOKUP_FAILED_MESSAGE;
        }
        else if (null != snmpServiceReturnMessage)
        {
            if (WebServiceReturnEnum.SUCCESS == snmpServiceReturnMessage.getResult())
            {
                result = snmpServiceReturnMessage.getResultObject();
            }
            else
            {
                result = snmpServiceReturnMessage.getServiceCode().toString();
            }
        }
        snmpForm.getResultTextFld().append(result + "\n");
    }
}
