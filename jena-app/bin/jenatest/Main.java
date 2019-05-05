package SDM.Lab3;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.Ontology;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.CardinalityRestriction;
import org.apache.jena.ontology.Individual;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.*;

public class Main {

	public static void main(String[] args) throws IOException {
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////
		//ONTOLOGY
		///////////////////////////////////////////////////////////////////////////////////////////////////////
		
		// Create an empty ontology model
		OntModel myOntology = ModelFactory.createOntologyModel();
		String name = new String("http://www.sdmlab3.com/my_ontology#");
		String URI = new String("http://www.sdmlab3.com/my_ontology");
		Ontology onto = myOntology.createOntology(URI);

		///////////////////////////////////////////////////////////////////////////////////////////////////////
		//CLASSES AND SUBCLASSES
		///////////////////////////////////////////////////////////////////////////////////////////////////////		
		
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
		OntClass databaseConference = myOntology.createClass(name + "DatabaseConference");
		OntClass openAccessJournal = myOntology.createClass(name + "OpenAccessJournal");
		
		// And now let's define subclasses
		person.addSubClass(author);
		author.addSubClass(reviewer);
		
		conference.addSubClass(databaseConference);
		journal.addSubClass(openAccessJournal);
		
		paper.addSubClass(shortPaper);
		paper.addSubClass(demoPaper);
		paper.addSubClass(surveyPaper);
		paper.addSubClass(fullPaper);
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////
		//DATA PROPERTIES
		///////////////////////////////////////////////////////////////////////////////////////////////////////
		
		// PAPERS, CONFERENCES AND JOURNALS ///////////////////////////////////////////////////////////////////
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
		
		// City 
		myOntology.read("http://dbpedia.org/ontology/cityLink");
		DatatypeProperty hasCity = myOntology.getDatatypeProperty("http://dbpedia.org/ontology/cityLink");
		hasCity.setDomain(conference);
		hasCity.setRange(XSD.xstring);
		
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
		
		//Topic of conferences and journals
		DatatypeProperty hasTopic = myOntology.createDatatypeProperty(name + "hasTopic");
		hasTopic.setDomain(conference);
		hasTopic.setDomain(journal);
		hasTopic.setRange(XSD.xstring);
		
		// Name of conferences, journals and organizations
		DatatypeProperty hasName = myOntology.createDatatypeProperty(name + "hasName");
		hasName.setDomain(conference);
		hasName.setDomain(journal);
		hasName.setDomain(organization);
		hasName.setRange(XSD.xstring);
		
		// AUTHORS ////////////////////////////////////////////////////////////////////////////////////////////
		// Name
		myOntology.read("http://dbpedia.org/ontology/birthName");
		DatatypeProperty hasBirthName = myOntology.getDatatypeProperty("http://dbpedia.org/ontology/birthName");
		hasBirthName.setDomain(author);
		hasBirthName.setDomain(reviewer);
		hasBirthName.setRange(XSD.xstring);
		
		// Gender
		myOntology.read("http://dbpedia.org/ontology/sex");
		DatatypeProperty hasGender = myOntology.getDatatypeProperty("http://dbpedia.org/ontology/sex");
		hasGender.setDomain(author);
		hasGender.setDomain(reviewer);
		hasGender.setRange(XSD.xstring);
		
		
		// REVIEWS ////////////////////////////////////////////////////////////////////////////////////////////
		// Decision of reviews
		DatatypeProperty hasDecision = myOntology.createDatatypeProperty(name + "hasDecision");
		hasDecision.setDomain(review);
		hasDecision.setRange(XSD.xstring);
		
		// Score of reviews
		DatatypeProperty hasScore = myOntology.createDatatypeProperty(name + "hasScore");
		hasScore.setDomain(review);
		hasScore.setRange(XSD.integer);


		///////////////////////////////////////////////////////////////////////////////////////////////////////
		//OBJECT PROPERTIES
		///////////////////////////////////////////////////////////////////////////////////////////////////////	
		
		// Paper citations
		ObjectProperty isCitedBy = myOntology.createObjectProperty(name + "isCitedBy");
		isCitedBy.setDomain(paper);
		isCitedBy.setRange(paper);
		
		// Paper co-authors
		ObjectProperty coAuthors = myOntology.createObjectProperty(name + "hasCoAuthors");
		coAuthors.setDomain(paper);
		coAuthors.setRange(author);
		
		// Paper corresponding author
		ObjectProperty correspondingAuthor = myOntology.createObjectProperty(name + "hasCorrespondingAuthor");
		correspondingAuthor.setDomain(paper);
		correspondingAuthor.setRange(author);
		
		// Papers in a conference or journal
		ObjectProperty hasPapers = myOntology.createObjectProperty(name + "hasPapers");
		hasPapers.setDomain(conference);
		hasPapers.setDomain(journal);
		hasPapers.setRange(paper);
		
		// Review author
		ObjectProperty hasReviewer = myOntology.createObjectProperty(name + "hasReviewer");
		hasReviewer.setDomain(review);
		hasReviewer.setRange(reviewer);
		
		// Review paper
		ObjectProperty ofPaper = myOntology.createObjectProperty(name + "ofPaper");
		ofPaper.setDomain(review);
		ofPaper.setRange(paper);
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////
		//CREATE RESTRICTIONS
		///////////////////////////////////////////////////////////////////////////////////////////////////////	
		
		// Only one corresponding author and is disjoint from co-authors
		CardinalityRestriction correspondingRestriction = myOntology.createCardinalityRestriction(null, correspondingAuthor, 1);
		correspondingRestriction.addDisjointWith(coAuthors);
		
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////
		//INSTANCES
		///////////////////////////////////////////////////////////////////////////////////////////////////////	
		
		// Create individuals
		//Individual John = author.createIndividual(name + "John Deere");
		
		///////////////////////////////////////////////////////////////////////////////////////////////////////
		//EXPORT FILE
		///////////////////////////////////////////////////////////////////////////////////////////////////////
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/java/SDM/Lab3/ontology.owl"));	    
			myOntology.write(writer);
			System.out.println("Listo!");
		}
		catch(IOException e) {
			System.out.println(e);
		}
		
	}
	

}
