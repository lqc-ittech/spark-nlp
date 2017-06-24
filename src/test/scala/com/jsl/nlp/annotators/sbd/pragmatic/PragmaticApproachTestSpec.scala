package com.jsl.nlp.annotators.sbd.pragmatic

import com.jsl.nlp.annotators.sbd.SentenceDetector
import com.jsl.nlp.{ContentProvider, DataBuilder}
import org.scalatest._

/**
  * Created by Saif Addin on 5/7/2017.
  */
class PragmaticApproachTestSpec extends FlatSpec with PragmaticDetectionBehaviors {

  val generalParagraphAns = Array(
    "In the third category he included those Brothers (the majority) who saw nothing in Freemasonry but the " +
      "external forms and ceremonies, and prized the strict performance of these forms without troubling about " +
      "their purport or significance.",
    "Such were Willarski and even the Grand Master of the principal lodge.",
    "Finally, to the fourth category also a great many Brothers belonged, particularly those who had lately joined.",
    "These according to Pierre's observations were men who had no belief in anything, nor desire for anything, " +
      "but joined the Freemasons merely to associate with the wealthy young Brothers who were influential through " +
      "their connections or rank, and of whom there were very many in the lodge.", "Pierre began to feel dissatisfied " +
      "with what he was doing.",
    "Freemasonry, at any rate as he saw it here, sometimes seemed to him based merely on externals.", "He did not think " +
      "of doubting Freemasonry itself, but suspected that Russian Masonry had taken a wrong path and deviated from its " +
      "original principles.",
    "And so toward the end of the year he went abroad to be initiated into the higher secrets of the order.",
    "What is to be done in these circumstances?",
    "To favor revolutions, overthrow everything, repel force by force?",
    "No!",
    "We are very far from that.",
    "Every violent reform deserves censure, for it quite fails to remedy evil while men remain what they are, and " +
      "also because wisdom needs no violence.",
    "\"But what is there in running across it like that?\" said Ilagin's groom.",
    "\"Once she had missed it and turned it away, any mongrel could take it,\" Ilagin was saying at the same time, " +
      "breathless from his gallop and his excitement."
  )

  val generalParagraph = ContentProvider.sbdTestParagraph
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(
    generalParagraph,
    generalParagraphAns
  )

  "an isolated pragmatic detector" should behave like isolatedPDReadScore(
    generalParagraph,
    generalParagraphAns
  )

  "a spark based pragmatic detector" should behave like sparkBasedSentenceDetector(
    DataBuilder.basicDataBuild(ContentProvider.sbdTestParagraph)
  )

  "A Pragmatic SBD" should "be readable and writable" in {
    val pragmaticDetector = new SentenceDetector().setModel(new PragmaticApproach)
    val path = "./test-output-tmp/pragmaticdetector"
    pragmaticDetector.write.overwrite.save(path)
    val pragmaticDetectorRead = SentenceDetector.read.load(path)
    assert(pragmaticDetector.getModel.description == pragmaticDetectorRead.getModel.description)
  }

  /**
    * Golden Rules from Pragmatic Sentence Detector
    * https://github.com/diasks2/pragmatic_segmenter
    */

  //Simple period to end sentence
  val simplePeriodAns = Array("Hello World.", "My name is Jonas.")
  val simplePeriod = "Hello World. My name is Jonas."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(simplePeriod, simplePeriodAns)

  //Question mark to end sentence
  val questionMarkAns = Array("What is your name?", "My name is Jonas.")
  val questionMark = "What is your name? My name is Jonas."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(questionMark, questionMarkAns)

  //Exclamation point to end sentence
  val exclamationAns = Array("There it is!", "I found it.")
  val exclamation = "There it is! I found it."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(exclamation, exclamationAns)

  //One letter upper case abbreviations
  val upperAbbrAns = Array("My name is Jonas E. Smith.")
  val upperAbbr = "My name is Jonas E. Smith."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(upperAbbr, upperAbbrAns)

  //One letter lower case abbreviations
  val lowerAbbrAns = Array("Please turn to p. 55.")
  val lowerAbbr = "Please turn to p. 55."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(lowerAbbr, lowerAbbrAns)

  //Two letter lower case abbreviations in the middle of a sentence
  val twoLowerAbbrAns = Array("Were Jane and co. at the party?")
  val twoLowerAbbr = "Were Jane and co. at the party?"
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(twoLowerAbbr, twoLowerAbbrAns)

  //Two letter upper case abbreviations in the middle of a sentence
  val twoUpperAbbrAns = Array("They closed the deal with Pitt, Briggs & Co. at noon.")
  val twoUpperAbbr = "They closed the deal with Pitt, Briggs & Co. at noon."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(twoUpperAbbr, twoUpperAbbrAns)

  //Two letter lower case abbreviations at the end of a sentence
  val twoLowerAbbrEndAns = Array("Let's ask Jane and co.", "They should know.")
  val twoLowerAbbrEnd = "Let's ask Jane and co. They should know."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(twoLowerAbbrEnd, twoLowerAbbrEndAns)

  //Two letter upper case abbreviations at the end of a sentence
  val twoUpperAbbrEndAns = Array("They closed the deal with Pitt, Briggs & Co.", "It closed yesterday.")
  val twoUpperAbbrEnd = "They closed the deal with Pitt, Briggs & Co. It closed yesterday."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(twoUpperAbbrEnd, twoUpperAbbrEndAns)

  //Two letter (prepositive) abbreviations
  val twoPrepositiveAns = Array("I can see Mt. Fuji from here.")
  val twoPrepositive = "I can see Mt. Fuji from here."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(twoPrepositive, twoPrepositiveAns)

  //Two letter (prepositive & postpositive) abbreviations
  val prepepostAns = Array("St. Michael's Church is on 5th st. near the light.")
  val prepepost = "St. Michael's Church is on 5th st. near the light."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(prepepost, prepepostAns)

  //Possesive two letter abbreviations
  val possessiveAns = Array("That is JFK Jr.'s book.")
  val possessive = "That is JFK Jr.'s book."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(possessive, possessiveAns)

  //Number as non sentence boundary
  val numberNonSentAns = Array("She has $100.00 in her bag.")
  val numberNonSent = "She has $100.00 in her bag."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(numberNonSent, numberNonSentAns)

  //Number as sentence boundary
  val numberSentAns = Array("She has $100.00.", "It is in her bag.")
  val numberSent = "She has $100.00. It is in her bag."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(numberSent, numberSentAns)

  //Parenthetical inside sentence
  val parentsAns = Array("He teaches science (He previously worked for 5 years as an engineer.) at the local " +
    "University.")
  val parents = "He teaches science (He previously worked for 5 years as an engineer.) at the local University."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(parents, parentsAns)

  //Single quotations inside sentence
  val singleQuotAns = Array("She turned to him, 'This is great.' she said.")
  val singleQuot = "She turned to him, 'This is great.' she said."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(singleQuot, singleQuotAns)

  //Double quotations inside sentence
  val doubleQuotAns = Array("She turned to him, \"This is great.\" she said.")
  val doubleQuot = "She turned to him, \"This is great.\" she said."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(doubleQuot, doubleQuotAns)

  //Double punctuation (exclamation point)
  val doublePunctAns = Array("Hello!!", "Long time no see.")
  val doublePunct = "Hello!! Long time no see."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(doublePunct, doublePunctAns)

  //Double punctuation (question mark)
  val doublePunctQuestAns = Array("Hello??", "Who is there?")
  val doublePunctQuest = "Hello?? Who is there?"
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(doublePunctQuest, doublePunctQuestAns)

  //Double punctuation (exclamation point / question mark)
  val doublePunctExcQuestAns = Array("Hello!?", "Is that you?")
  val doublePunctExcQuest = "Hello!? Is that you?"
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(doublePunctExcQuest, doublePunctExcQuestAns)

  //Double punctuation (question mark / exclamation point)
  val doublePunctQuestExcAns = Array("Hello?!", "Is that you?")
  val doublePunctQuestExc = "Hello?! Is that you?"
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(doublePunctQuestExc, doublePunctQuestExcAns)

  //List (period followed by parens and no period to end item)
  val listParensAns = Array("1.) The first item", "2.) The second item")
  val listParens = "1.) The first item 2.) The second item"
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(listParens, listParensAns)

  //List (period followed by parens and period to end item)
  val listParensPeriodAns = Array("1.) The first item.", "2.) The second item.")
  val listParensPeriod = "1.) The first item. 2.) The second item."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(listParensPeriod, listParensPeriodAns)

  //List (parens and period to end item)
  val listParensPeriodEndAns = Array("1) The first item.", "2) The second item.")
  val listParensPeriodEnd = "1) The first item. 2) The second item."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(listParensPeriodEnd, listParensPeriodEndAns)

  //Errant newline in the middle of a sentence (PDF)
  val errantNewlineAns = Array("This is a sentence\ncut off in the middle because pdf.")
  val errantNewline = "This is a sentence\ncut off in the middle because pdf."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(errantNewline, errantNewlineAns)

  //Named entities with an exclamation point
  val namedEntitiesAns = Array("She works at Yahoo! in the accounting department.")
  val namedEntities = "She works at Yahoo! in the accounting department."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(namedEntities, namedEntitiesAns)

  //Ellipsis at end of quotation
  val ellipsisAns = Array("Thoreau argues that by simplifying one’s life, “the laws of the universe will " +
    "appear less complex. . . .”")
  val ellipsis = "Thoreau argues that by simplifying one’s life, “the laws of the universe will appear less " +
    "complex. . . .”"
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(ellipsis, ellipsisAns)

  //Ellipsis with square brackets
  val ellipsisSquareBrAns = Array("\"Bohr [...] used the analogy of parallel stairways [...]\" (Smith 55).")
  val ellipsisSquareBr = "\"Bohr [...] used the analogy of parallel stairways [...]\" (Smith 55)."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(ellipsisSquareBr, ellipsisSquareBrAns)

  //Multi-period abbreviations in the middle of a sentence
  val multiperAns = Array("I visited the U.S.A. last year.")
  val multiper = "I visited the U.S.A. last year."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(multiper, multiperAns)

  //Multi-period abbreviations at the end of a sentence
  val multiperEndAns = Array("I live in the E.U.", "How about you?")
  val multiperEnd = "I live in the E.U. How about you?"
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(multiperEnd, multiperEndAns)

  //U.S. as sentence boundary
  val USBoundAns = Array("I live in the U.S.", "How about you?")
  val USBound = "I live in the U.S. How about you?"
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(USBound, USBoundAns)

  //U.S. as non sentence boundary with next word capitalized
  val USBoundNextCapAns = Array("I work for the U.S. government in Virginia.")
  val USBoundNextCap = "I work for the U.S. government in Virginia."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(USBoundNextCap, USBoundNextCapAns)

  //U.S. as non sentence boundary
  val USNonSentenceAns = Array("I have lived in the U.S. for 20 years.")
  val USNonSentence = "I have lived in the U.S. for 20 years."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(USNonSentence, USNonSentenceAns)

  /*
  //A.M. / P.M. as non sentence boundary and sentence boundary
  val AMPMAns = Array("At 5 a.m. Mr. Smith went to the bank.", "He left the bank at 6 P.M.", "Mr. Smith " +
    "then went to the store.")
  val AMPM = "At 5 a.m. Mr. Smith went to the bank. He left the bank at 6 P.M. Mr. Smith then went to the store."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(AMPM, AMPMAns)
  */

  //Email addresses
  val emailAns = Array("Her email is Jane.Doe@example.com.", "I sent her an email.")
  val email = "Her email is Jane.Doe@example.com. I sent her an email."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(email, emailAns)

  //Web addresses
  val webAns = Array("The site is: https://www.example.50.com/new-site/awesome_content.html.", "Please check " +
    "it out.")
  val web = "The site is: https://www.example.50.com/new-site/awesome_content.html. Please check it out."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(web, webAns)

  /*
  //Double quotations at the end of a sentence
  val doubleQuotEndAns = Array("She turned to him, \"This is great.\"", "She held the book out to show him.")
  val doubleQuotEnd = "She turned to him, \"This is great.\" She held the book out to show him."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(doubleQuotEnd, doubleQuotEndAns)
  */

  /*
  //List (parens and no period to end item)
  val listParensNoPeriodAns = Array("1) The first item", "2) The second item")
  val listParensNoPeriod = "1) The first item 2) The second item"
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(listParensNoPeriod, listParensNoPeriodAns)
  */

  /*
  //List (period to mark list and no period to end item)
  val listPeriodMarkAns = Array("1. The first item", "2. The second item")
  val listPeriodMark = "1. The first item 2. The second item"
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(listPeriodMark, listPeriodMarkAns)

  //List (period to mark list and period to end item)
  val listPeriodMarkEndAns = Array("1. The first item.", "2. The second item.")
  val listPeriodMarkEnd = "1. The first item. 2. The second item."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(listPeriodMarkEnd, listPeriodMarkEndAns)

  //List with bullet
  val listBulletAns = Array("• 9. The first item", "• 10. The second item")
  val listBullet = "• 9. The first item • 10. The second item"
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(listBullet, listBulletAns)

  //List with hypthen
  val listHypthenAns = Array("⁃9. The first item", "⁃10. The second item")
  val listHypthen = "⁃9. The first item ⁃10. The second item"
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(listHypthen, listHypthenAns)

  //Alphabetical list
  val listAlphaAns = Array("a. The first item", "b. The second item", "c. The third list item")
  val listAlpha = "a. The first item b. The second item c. The third list item"
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(listAlpha, listAlphaAns)
  */

  /*
  //Errant newline in the middle of a sentence
  val errantNewlineMiddleAns = Array("It was a cold night in the city.")
  val errantNewlineMiddle = "It was a cold \nnight in the city."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(errantNewlineMiddle, errantNewlineMiddleAns)

  //Lower case list separated by newline
  val lowerCaseListAns = Array("features", "contact manager", "events, activities")
  val lowerCaseList = "features\ncontact manager\nevents, activities\n"
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(lowerCaseList, lowerCaseListAns)

  //Geo Coordinates
  val geoCoordsAns = Array("You can find it at N°. 1026.253.553.", "That is where the treasure is.")
  val geoCoords = "You can find it at N°. 1026.253.553. That is where the treasure is."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(geoCoords, geoCoordsAns)
  */

  /*
  //Ellipsis as sentence boundary (standard ellipsis rules)
  val ellipsisBoundaryAns = Array("If words are left off at the end of a sentence, and that is all that is omitted," +
    " indicate the omission with ellipsis marks (preceded and followed by a space) and then indicate the end of" +
    " the sentence with a period . . . .", "Next sentence.")
  val ellipsisBoundary = "If words are left off at the end of a sentence, and that is all that is omitted," +
    "indicate the omission with ellipsis marks (preceded and followed by a space) and then indicate the" +
    "end of the sentence with a period . . . . Next sentence."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(ellipsisBoundary, ellipsisBoundaryAns)
  */

  /*
  //I as a sentence boundary and I as an abbreviation
  val IConfusingAns = Array("We make a good team, you and I.", "Did you see Albert I. Jones yesterday?")
  val IConfusing = "We make a good team, you and I. Did you see Albert I. Jones yesterday?"
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(IConfusing, IConfusingAns)
  */

  /*
  //Ellipsis as sentence boundary (non-standard ellipsis rules)
  val ellipsisNoStAns = Array("I never meant that....", "She left the store.")
  val ellipsisNoSt = "I never meant that.... She left the store."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(ellipsisNoSt, ellipsisNoStAns)

  //Ellipsis as non sentence boundary
  val ellipsisNonBoundAns = Array("I wasn’t really ... well, what I mean...see . . . what I'm saying, the thing " +
    "is . . . I didn’t mean it.")
  val ellipsisNonBound = "I wasn’t really ... well, what I mean...see . . . what I'm saying, the thing is . . . " +
    "I didn’t mean it."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(ellipsisNonBound, ellipsisNonBoundAns)

  //4-dot ellipsis
  val ellipsis4dotAns = Array("One further habit which was somewhat weakened . . . was that of combining words " +
    "into self-interpreting compounds.", ". . . The practice was not abandoned. . . .")
  val ellipsis4dot = "One further habit which was somewhat weakened . . . was that of combining words " +
    "into self-interpreting compounds. . . . The practice was not abandoned. . . ."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(ellipsis4dot, ellipsis4dotAns)

  //No whitespace in between sentences Credit: Don_Patrick
  val noWhiteSpaceAns = Array("Hello world.", "Today is Tuesday.", "Mr. Smith went to the store and bought " +
    "1,000.", "That is a lot.")
  val noWhiteSpace = "Hello world.Today is Tuesday.Mr. Smith went to the store and bought 1,000.That is a lot."
  "an isolated pragmatic detector" should behave like isolatedPDReadAndMatchResult(noWhiteSpace, noWhiteSpaceAns)
  */
}
