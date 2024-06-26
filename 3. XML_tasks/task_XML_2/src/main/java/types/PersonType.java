
package types;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlID;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.CollapsedStringAdapter;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for person-type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="person-type"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="spouсe" type="{}id-type" minOccurs="0"/&gt;
 *         &lt;element name="parents" type="{}parents-type" minOccurs="0"/&gt;
 *         &lt;element name="siblings" type="{}siblings-type" minOccurs="0"/&gt;
 *         &lt;element name="children" type="{}children-type" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" /&gt;
 *       &lt;attribute name="name" use="required" type="{}name-type" /&gt;
 *       &lt;attribute name="gender" use="required" type="{}gender-type" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "person-type", propOrder = {
    "spou\u0441e",
    "parents",
    "siblings",
    "children"
})
public class PersonType {

    protected IdType spouсe;
    protected ParentsType parents;
    protected SiblingsType siblings;
    protected ChildrenType children;
    @XmlAttribute(name = "id", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "gender", required = true)
    protected GenderType gender;

    /**
     * Gets the value of the spouсe property.
     * 
     * @return
     *     possible object is
     *     {@link IdType }
     *     
     */
    public IdType getSpouce() {
        return spouсe;
    }

    /**
     * Sets the value of the spouсe property.
     * 
     * @param value
     *     allowed object is
     *     {@link IdType }
     *     
     */
    public void setSpouce(IdType value) {
        this.spouсe = value;
    }

    /**
     * Gets the value of the parents property.
     * 
     * @return
     *     possible object is
     *     {@link ParentsType }
     *     
     */
    public ParentsType getParents() {
        return parents;
    }

    /**
     * Sets the value of the parents property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParentsType }
     *     
     */
    public void setParents(ParentsType value) {
        this.parents = value;
    }

    /**
     * Gets the value of the siblings property.
     * 
     * @return
     *     possible object is
     *     {@link SiblingsType }
     *     
     */
    public SiblingsType getSiblings() {
        return siblings;
    }

    /**
     * Sets the value of the siblings property.
     * 
     * @param value
     *     allowed object is
     *     {@link SiblingsType }
     *     
     */
    public void setSiblings(SiblingsType value) {
        this.siblings = value;
    }

    /**
     * Gets the value of the children property.
     * 
     * @return
     *     possible object is
     *     {@link ChildrenType }
     *     
     */
    public ChildrenType getChildren() {
        return children;
    }

    /**
     * Sets the value of the children property.
     * 
     * @param value
     *     allowed object is
     *     {@link ChildrenType }
     *     
     */
    public void setChildren(ChildrenType value) {
        this.children = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the gender property.
     * 
     * @return
     *     possible object is
     *     {@link GenderType }
     *     
     */
    public GenderType getGender() {
        return gender;
    }

    /**
     * Sets the value of the gender property.
     * 
     * @param value
     *     allowed object is
     *     {@link GenderType }
     *     
     */
    public void setGender(GenderType value) {
        this.gender = value;
    }
}
