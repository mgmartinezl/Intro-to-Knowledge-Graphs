package SDM.Lab3;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.Ontology;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.ontology.CardinalityRestriction;
import org.apache.jena.ontology.Individual;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.*;

public class Main {

	
	
	public static void main(String[] args) throws IOException {
		
		// Create an empty ontology model and define name and URI
		OntModel myOntology = ModelFactory.createOntologyModel();
		String name = new String("http://www.sdmlab3.com/my_ontology#");
		String URI = new String("http://www.sdmlab3.com/my_ontology");
		
		// Define path to files
		String path = "src/main/java/SDM/Lab3/";
		
		// Create TBOX and ABOX
		createTBOX(myOntology, name);
		createABOX(myOntology, name, path);
		
		// Export to file
		exportOntology(myOntology, path);
	}
	
	public static void createTBOX(OntModel myOntology, String name) {
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
		
		myOntology.read("http://dbpedia.org/ontology/Organisation");
		OntClass organization = myOntology.getOntClass("http://dbpedia.org/ontology/Organisation");
		
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
		// Author organization
		ObjectProperty isInOrganization = myOntology.createObjectProperty(name + "isInOrganization");
		isInOrganization.setDomain(person);
		isInOrganization.setRange(organization);
		
		
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
		
		// If review exists it must have a reviewer and paper
		myOntology.createCardinalityRestriction(null, hasReviewer, 1);
		myOntology.createCardinalityRestriction(null, ofPaper, 1);		
	}
	
	public static void createABOX(OntModel myOntology, String name, String path) {
		try {
			String line;
			
			///////////////////////////////////////////////////////////////////////////////////////////////////////
			//AUTHORS
			///////////////////////////////////////////////////////////////////////////////////////////////////////
			
			BufferedReader br = new BufferedReader(new FileReader(path+"data/author.txt"));
			OntClass classAuthor = myOntology.getOntClass("http://dbpedia.org/ontology/Writer");
			DatatypeProperty hasBirthName = myOntology.getDatatypeProperty("http://dbpedia.org/ontology/birthName");
			br.readLine();
			
			while ((line = br.readLine()) != null) {
				String[] authorData = line.split(",");
				String goodName = authorData[1].replace(" #", "_");
				 
	            // Create individual
	            Individual author = classAuthor.createIndividual(name + goodName);
	            Literal authorName = myOntology.createTypedLiteral(name + goodName,XSDDatatype.XSDstring);
	            
	            // Create the statement
	            Statement authorNameIs = myOntology.createStatement(author, hasBirthName, authorName);
	            myOntology.add(authorNameIs);
			}
			
			///////////////////////////////////////////////////////////////////////////////////////////////////////
			//PAPERS
			///////////////////////////////////////////////////////////////////////////////////////////////////////
			
			br = new BufferedReader(new FileReader(path+"data/paper.txt"));
			OntClass shortPaper = myOntology.createClass(name + "ShortPaper");
			OntClass demoPaper = myOntology.createClass(name + "DemoPaper");
			OntClass surveyPaper = myOntology.createClass(name + "SurveyPaper");
			OntClass fullPaper = myOntology.createClass(name + "FullPaper");
			DatatypeProperty hasTitle = myOntology.getDatatypeProperty(name + "hasTitle");
			int count=0;
			br.readLine();
			
			while ((line = br.readLine()) != null) {
				String[] paperData = line.split(",");
				String goodTitle = paperData[1].replace(" #", "_");
				
				// Create individual
				Individual paper;
				switch(count%4) {
				case 0:
					paper = shortPaper.createIndividual(name + goodTitle);
					break;
				case 1:
					paper = demoPaper.createIndividual(name + goodTitle);
					break;
				case 2:
					paper = surveyPaper.createIndividual(name + goodTitle);
					break;
				default:
					paper = fullPaper.createIndividual(name + goodTitle);
					break;
				}
	            Literal paperTitle = myOntology.createTypedLiteral(name + goodTitle,XSDDatatype.XSDstring);
	            
	            // Create the statement
	            Statement paperTitleIs = myOntology.createStatement(paper, hasTitle, paperTitle);
	            myOntology.add(paperTitleIs);
	            
	            count++;
			}
			
			///////////////////////////////////////////////////////////////////////////////////////////////////////
			//JOURNALS
			///////////////////////////////////////////////////////////////////////////////////////////////////////
			
			br = new BufferedReader(new FileReader(path+"data/journals.txt"));
			OntClass classJournal = myOntology.getOntClass("http://dbpedia.org/ontology/AcademicJournal");
            DatatypeProperty hasName = myOntology.getDatatypeProperty(name + "hasName");
			br.readLine();
			
			while ((line = br.readLine()) != null) {
				String[] journalData = line.split(",");
				String goodName = journalData[1].replace(" #", "_");
				
				// Create individual
	            Individual journal = classJournal.createIndividual(name + goodName); 
	            Literal journalName = myOntology.createTypedLiteral(name + goodName,XSDDatatype.XSDstring);
	            
	            // Create the statement
	            Statement journalNameIs = myOntology.createStatement(journal, hasName, journalName);
	            myOntology.add(journalNameIs);
			}
			
			///////////////////////////////////////////////////////////////////////////////////////////////////////
			//CONFERENCES
			///////////////////////////////////////////////////////////////////////////////////////////////////////
			
			br = new BufferedReader(new FileReader(path+"data/conferences.txt"));
			OntClass classConference = myOntology.getOntClass("http://dbpedia.org/ontology/AcademicConference");
			//reuse datatype property hasName
			br.readLine();
			
			while ((line = br.readLine()) != null) {
				String[] conferenceData = line.split(",");
				String goodName = conferenceData[1].replace(" #", "_");
				
				// Create individual
	            Individual conference = classConference.createIndividual(name + goodName); 
	            Literal conferenceName = myOntology.createTypedLiteral(name + goodName,XSDDatatype.XSDstring);
	            
	            // Create the statement
	            Statement conferenceNameIs = myOntology.createStatement(conference, hasName, conferenceName);
	            myOntology.add(conferenceNameIs);
			}
			
			///////////////////////////////////////////////////////////////////////////////////////////////////////
			//ORGANIZATIONS
			///////////////////////////////////////////////////////////////////////////////////////////////////////
			
			br = new BufferedReader(new FileReader(path+"data/organization.txt"));
			OntClass classOrganization = myOntology.getOntClass("http://dbpedia.org/ontology/Company");
			//reuse datatype property hasName
			br.readLine();
			
			while ((line = br.readLine()) != null) {
				String[] organizationData = line.split(",");
				String goodName = organizationData[1].replace(" #", "_");
				
				// Create individual
	            Individual organization = classOrganization.createIndividual(name + goodName); 
	            Literal organizationName = myOntology.createTypedLiteral(name + goodName,XSDDatatype.XSDstring);
	            
	            // Create the statement
	            Statement organizationNameIs = myOntology.createStatement(organization, hasName, organizationName);
	            myOntology.add(organizationNameIs);
			}
			
			///////////////////////////////////////////////////////////////////////////////////////////////////////
			//PAPER HAS AUTHORS
			///////////////////////////////////////////////////////////////////////////////////////////////////////
			
			br = new BufferedReader(new FileReader(path+"data/writes_relation.txt"));
			ObjectProperty coAuthors = myOntology.createObjectProperty(name + "hasCoAuthors");
			ObjectProperty correspondingAuthor = myOntology.createObjectProperty(name + "hasCorrespondingAuthor");
			br.readLine();
			
			while ((line = br.readLine()) != null) {
				String[] authorshipData = line.split(",");
				
				// Create individual
	            Individual paper = myOntology.getIndividual(name + "Paper_" + authorshipData[0]);
	            Individual author = myOntology.getIndividual(name + "Author_" + authorshipData[1]);
	            
	            // Create the statement
	            Statement paperHasAuthor;
	            if(authorshipData[2].equals("Yes"))
            		paperHasAuthor = myOntology.createStatement(paper, correspondingAuthor, author);
	            else
            		paperHasAuthor = myOntology.createStatement(paper, coAuthors, author);
	            myOntology.add(paperHasAuthor);
			}
			
			///////////////////////////////////////////////////////////////////////////////////////////////////////
			//CITES
			///////////////////////////////////////////////////////////////////////////////////////////////////////
			
			br = new BufferedReader(new FileReader(path+"data/citedBy_relation.txt"));
			ObjectProperty isCitedBy = myOntology.getObjectProperty(name + "isCitedBy");
			br.readLine();
			
			while ((line = br.readLine()) != null) {
				String[] paperData = line.split(",");
				
				// Create individual
	            Individual paper = myOntology.getIndividual(name + "Paper_" + paperData[0]);
	            Individual citingPaper = myOntology.getIndividual(name + "Paper_" + paperData[1]);
	            
	            // Create the statement
	            Statement paperIsCited = myOntology.createStatement(paper, isCitedBy, citingPaper);
	            myOntology.add(paperIsCited);
			}
			
			///////////////////////////////////////////////////////////////////////////////////////////////////////
			//KEYWORDS
			///////////////////////////////////////////////////////////////////////////////////////////////////////
			
			br = new BufferedReader(new FileReader(path+"data/contains_relation.txt"));
			DatatypeProperty hasKeywords = myOntology.getDatatypeProperty(name + "hasKeywords");
			br.readLine();
			
			while ((line = br.readLine()) != null) {
				String[] keywordData = line.split(",");
				String goodKeyword = keywordData[2].replace(" ", "_");
				
				// Create individual
	            Individual paper = myOntology.getIndividual(name + "Paper_" + keywordData[0]);
	            Literal keyword = myOntology.createTypedLiteral(name + goodKeyword,XSDDatatype.XSDstring);
	            
	            // Create the statement
	            Statement paperHasKeywords = myOntology.createStatement(paper, hasKeywords, keyword);
	            myOntology.add(paperHasKeywords);
			}
			
			///////////////////////////////////////////////////////////////////////////////////////////////////////
			//AUTHOR IN ORGANIZATION
			///////////////////////////////////////////////////////////////////////////////////////////////////////
			
			br = new BufferedReader(new FileReader(path+"data/contains_relation.txt"));
			ObjectProperty isInOrganization = myOntology.getObjectProperty(name + "isInOrganization");
			br.readLine();
			
			while ((line = br.readLine()) != null) {
				String[] belongsData = line.split(",");
				
				// Create individual
	            Individual author = myOntology.getIndividual(name + "Author_" + belongsData[1]);
	            Individual organization = myOntology.getIndividual(name + "Organization_" + belongsData[0]);
	            
	            // Create the statement
	            Statement authorIsInOrganization = myOntology.createStatement(author, isInOrganization, organization);
	            myOntology.add(authorIsInOrganization);
			}
			
			///////////////////////////////////////////////////////////////////////////////////////////////////////
			//PAPER IN CONFERENCE
			///////////////////////////////////////////////////////////////////////////////////////////////////////
			
			br = new BufferedReader(new FileReader(path+"data/contains_relation.txt"));
			ObjectProperty hasPapers = myOntology.getObjectProperty(name + "hasPapers");
			br.readLine();
			
			while ((line = br.readLine()) != null) {
				String[] containsData = line.split(",");
				
				// Create individual
	            Individual paper = myOntology.getIndividual(name + "Paper_" + containsData[0]);
	            Individual conference = myOntology.getIndividual(name + "Conference_" + containsData[1]);
	            
	            // Create the statement
	            Statement conferenceHasPapers = myOntology.createStatement(conference, hasPapers, paper);
	            myOntology.add(conferenceHasPapers);
			}
			
			///////////////////////////////////////////////////////////////////////////////////////////////////////
			//PAPER IN JOURNAL
			///////////////////////////////////////////////////////////////////////////////////////////////////////
			
			br = new BufferedReader(new FileReader(path+"data/publishes_relation.txt"));
			// reuse hasPapers 
			br.readLine();
			
			while ((line = br.readLine()) != null) {
				String[] containsData = line.split(",");
				
				// Create individual
				Individual paper = myOntology.getIndividual(name + "Paper_" + containsData[0]);
				Individual journal = myOntology.getIndividual(name + "Journal_" + containsData[1]);
				
				// Create the statement
				Statement journalHasPapers = myOntology.createStatement(journal, hasPapers, paper);
				myOntology.add(journalHasPapers);
			}
			
			///////////////////////////////////////////////////////////////////////////////////////////////////////
			//REVIEWS
			///////////////////////////////////////////////////////////////////////////////////////////////////////
			
			br = new BufferedReader(new FileReader(path+"data/reviews_relation.txt.txt"));
			OntClass classReview = myOntology.getOntClass(name + "Review");
			OntClass classReviewer = myOntology.getOntClass(name + "Reviewer");
			ObjectProperty hasReviewer = myOntology.getObjectProperty(name + "hasReviewer");
			ObjectProperty ofPaper = myOntology.getObjectProperty(name + "ofPaper");
			count=0;
			br.readLine();
			
			
			while ((line = br.readLine()) != null) {
				String[] reviewData = line.split(",");
				
				// Create individual
				Individual review = classReview.createIndividual(name + "Review_" + count);
				Individual reviewer = classReviewer.createIndividual(name + "Reviewer_" + reviewData[1]);
				Individual paper = myOntology.getIndividual(name + "Paper_" + reviewData[0]);
				
				// Create the statements
				Statement reviewHasReviewer = myOntology.createStatement(review, hasReviewer, reviewer);
				Statement reviewOfPaper = myOntology.createStatement(review, ofPaper, paper);
				myOntology.add(reviewHasReviewer);
				myOntology.add(reviewOfPaper);
				
				count++;
			}
			
		}
		catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static void exportOntology(OntModel myOntology, String path) throws IOException{
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(path+"myOntology.owl"));	    
			myOntology.write(writer);
			System.out.println("Done!");
		}
		catch(IOException e) {
			System.out.println(e);
		}
	}

}
