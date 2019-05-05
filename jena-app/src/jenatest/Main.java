package jenatest;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.Ontology;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.*;

public class Main {

	public static void main(String[] args) {
		
		// Create an empty ontology model
		OntModel myOntology = ModelFactory.createOntologyModel();
		String name = new String("http://www.sdmlab3.com/my_ontology#");
		String URI = new String("http://www.sdmlab3.com/my_ontology");
		Ontology onto = myOntology.createOntology(URI);

		//Let's create the classes		
		
		//We will import some classes from DBPedia
		myOntology.read("http://dbpedia.org/ontology/Person");
		OntClass person = myOntology.getOntClass("http://dbpedia.org/ontology/Person");
		
		myOntology.read("http://dbpedia.org/ontology/Writer");
		OntClass author = myOntology.getOntClass("http://dbpedia.org/ontology/Writer");
		
		myOntology.read("http://dbpedia.org/ontology/Article");
		OntClass paper = myOntology.getOntClass("http://dbpedia.org/ontology/Article");
		
		myOntology.read("http://dbpedia.org/ontology/AcademicJournal");
		OntClass journal = myOntology.getOntClass("http://dbpedia.org/ontology/AcademicJournal");
		
		myOntology.read("http://dbpedia.org/ontology/AcademicConference");
		OntClass conference = myOntology.getOntClass("http://dbpedia.org/ontology/AcademicConference");
		
		myOntology.read("http://dbpedia.org/ontology/Company");
		OntClass organization = myOntology.getOntClass("http://dbpedia.org/ontology/Company");
		
		// Rest of the classes will be created by us
		OntClass reviewer = myOntology.createClass(name + "Reviewer");
		OntClass review = myOntology.createClass(name + "Review");
		OntClass shortPaper = myOntology.createClass(name + "ShortPaper");
		OntClass demoPaper = myOntology.createClass(name + "DemoPaper");
		OntClass surveyPaper = myOntology.createClass(name + "SurveyPaper");
		OntClass fullPaper = myOntology.createClass(name + "FullPaper");
		
		// And now let's define some constraints
		person.addSubClass(author);
		person.addSubClass(reviewer);
		
		paper.addSubClass(shortPaper);
		paper.addSubClass(demoPaper);
		paper.addSubClass(surveyPaper);
		paper.addSubClass(fullPaper);
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////
		
		//Create properties and their domain/range
		
		// CONFERENCES, JOURNALS AND PAPERS ///////////////////////////////////////////////////////////////////
		
		// Paper citations
		DatatypeProperty isCitedBy = myOntology.createDatatypeProperty(name + "isCitedBy");
		isCitedBy.setDomain(paper);
		isCitedBy.setRange(paper);
		
		// Paper keywords
		DatatypeProperty hasKeywords = myOntology.createDatatypeProperty(name + "hasKeywords");
		hasKeywords.setDomain(paper);
		hasKeywords.setRange(XSD.xstring);
		
		// Paper name
		DatatypeProperty hasTitle = myOntology.createDatatypeProperty(name + "hasTitle");
		hasTitle.setDomain(paper);
		hasTitle.setRange(XSD.xstring);
		
		// Abstract of conferences and journals
		myOntology.read("http://dbpedia.org/ontology/abstract");
		DatatypeProperty hasAbstract = myOntology.getDatatypeProperty("http://dbpedia.org/ontology/abstract");
		hasAbstract.setDomain(paper);
		//hasAbstract.setDomain(conference); // Not available for conferences
		hasAbstract.setRange(XSD.xstring);
		 
		// Month
		DatatypeProperty hasMonth = myOntology.createDatatypeProperty(name + "hasMonth");
		hasMonth.setDomain(conference);
		hasMonth.setRange(XSD.gMonth);
		/*
		// City THIS HAS A NULLPOINTER EXCEPTION
		myOntology.read("http://dbpedia.org/ontology/Place");
		DatatypeProperty hasCity = myOntology.getDatatypeProperty("http://dbpedia.org/ontology/Place");
		hasCity.setDomain(conference);
		hasCity.setRange(XSD.xstring);
		*/
		// Edition
		DatatypeProperty hasEdition = myOntology.createDatatypeProperty(name + "hasEdition");
		hasEdition.setDomain(conference);
		hasEdition.setRange(XSD.integer);
		
		// Volume
		DatatypeProperty hasVolume = myOntology.createDatatypeProperty(name + "hasVolume");
		hasVolume.setDomain(journal);
		hasVolume.setRange(XSD.integer);
		
		// Year
		DatatypeProperty hasYear = myOntology.createDatatypeProperty(name + "hasYear");
		hasYear.setDomain(conference);
		hasYear.setDomain(journal);
		hasYear.setRange(XSD.gYear);
		
		/* THIS HAS A NULLPOINTER EXCEPTION
		// Topic of conferences and journals
		myOntology.read("http://purl.org/dc/terms/subject");
		DatatypeProperty hasSubject = myOntology.getDatatypeProperty("http://purl.org/dc/terms/subject");
		hasSubject.setDomain(conference);
		hasSubject.setDomain(journal);
		hasSubject.setRange(XSD.xstring);
		*/
		/* THIS HAS A NULLPOINTER EXCEPTION
		// Name of conferences and journals
		myOntology.read("http://linguistics-ontology.org/gold/hypernym");
		DatatypeProperty hasHypernym = myOntology.getDatatypeProperty("http://linguistics-ontology.org/gold/hypernym");
		hasHypernym.setDomain(conference);
		hasHypernym.setDomain(journal);
		hasHypernym.setRange(XSD.xstring);
		*/
		
		// AUTHORS ///////////////////////////////////////////////////////////////////////////////////////////
		
		// Name
		myOntology.read("http://dbpedia.org/ontology/birthName");
		DatatypeProperty hasName = myOntology.getDatatypeProperty("http://dbpedia.org/ontology/birthName");
		hasName.setDomain(author);
		hasName.setDomain(reviewer);
		hasName.setRange(XSD.xstring);
		
		// Gender
		myOntology.read("http://dbpedia.org/ontology/sex");
		DatatypeProperty hasGender = myOntology.getDatatypeProperty("http://dbpedia.org/ontology/sex");
		hasGender.setDomain(author);
		hasGender.setDomain(reviewer);
		
		
		
		// REVIEWS //////////////////////////////////////////////////////////////////////////////////////////
		
		// Decision of reviews
		DatatypeProperty hasDecision = myOntology.createDatatypeProperty(name + "hasDecision");
		hasDecision.setDomain(review);
		hasDecision.setRange(XSD.xstring);
		
		// Score of reviews
		DatatypeProperty hasScore = myOntology.createDatatypeProperty(name + "hasScore");
		hasScore.setDomain(review);
		hasScore.setRange(XSD.integer);
	
		////////////////////////////////////////////////////////////////////////////////////////////////////
		
		// Create individuals
		//Individual John = author.createIndividual(name + "John Deere");
		
		
		myOntology.write(System.out);
	
		
	}
	

}
