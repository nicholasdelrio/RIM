package edu.utep.cybershare.rim.ontology.impl;

import edu.utep.cybershare.rim.ontology.*;

import java.util.Collection;

import org.protege.owl.codegeneration.WrappedIndividual;
import org.protege.owl.codegeneration.impl.WrappedIndividualImpl;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;


/**
 * Generated by Protege (http://protege.stanford.edu).<br>
 * Source Class: DefaultResource <br>
 * @version generated on Mon Jun 09 09:34:58 GMT-07:00 2014 by nick
 */
public class DefaultResource extends WrappedIndividualImpl implements Resource {

    public DefaultResource(OWLOntology ontology, IRI iri) {
        super(ontology, iri);
    }





    /* ***************************************************
     * Object Property http://ontology.cybershare.utep.edu/RIM/rim.owl#relatedToResource
     */
     
    public Collection<? extends Resource> getRelatedToResource() {
        return getDelegate().getPropertyValues(getOwlIndividual(),
                                               Vocabulary.OBJECT_PROPERTY_RELATEDTORESOURCE,
                                               DefaultResource.class);
    }

    public boolean hasRelatedToResource() {
	   return !getRelatedToResource().isEmpty();
    }

    public void addRelatedToResource(Resource newRelatedToResource) {
        getDelegate().addPropertyValue(getOwlIndividual(),
                                       Vocabulary.OBJECT_PROPERTY_RELATEDTORESOURCE,
                                       newRelatedToResource);
    }

    public void removeRelatedToResource(Resource oldRelatedToResource) {
        getDelegate().removePropertyValue(getOwlIndividual(),
                                          Vocabulary.OBJECT_PROPERTY_RELATEDTORESOURCE,
                                          oldRelatedToResource);
    }


}