package edu.txstate.cs4398.vc.model;

import javax.xml.bind.annotation.XmlEnum;

/**
 * An enumeration of movie ratings.
 * 
 * @author Ed
 */
@XmlEnum
public enum Rating {
	G, PG, PG13, R, NC17, NR;
}