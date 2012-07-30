
package edu.txstate.cs4398.vc.desktop.services;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for rating.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="rating">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="G"/>
 *     &lt;enumeration value="PG"/>
 *     &lt;enumeration value="PG13"/>
 *     &lt;enumeration value="R"/>
 *     &lt;enumeration value="NC17"/>
 *     &lt;enumeration value="NR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "rating")
@XmlEnum
public enum Rating {

    G("G"),
    PG("PG"),
    @XmlEnumValue("PG13")
    PG_13("PG13"),
    R("R"),
    @XmlEnumValue("NC17")
    NC_17("NC17"),
    NR("NR");
    private final String value;

    Rating(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Rating fromValue(String v) {
        for (Rating c: Rating.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
