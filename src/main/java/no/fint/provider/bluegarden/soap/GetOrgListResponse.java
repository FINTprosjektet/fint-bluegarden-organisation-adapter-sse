
package no.fint.provider.bluegarden.soap;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://bluegarden.no/organisation/structure/object/v31}OrgList" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "orgList"
})
@XmlRootElement(name = "getOrgListResponse", namespace = "http://bluegarden.no/organisation/structure/service/v31")
public class GetOrgListResponse {

    @XmlElement(name = "OrgList", namespace = "http://bluegarden.no/organisation/structure/object/v31")
    protected List<OrgListObject> orgList;

    /**
     * Gets the value of the orgList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the orgList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOrgList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link OrgListObject }
     * 
     * 
     */
    public List<OrgListObject> getOrgList() {
        if (orgList == null) {
            orgList = new ArrayList<OrgListObject>();
        }
        return this.orgList;
    }

}
