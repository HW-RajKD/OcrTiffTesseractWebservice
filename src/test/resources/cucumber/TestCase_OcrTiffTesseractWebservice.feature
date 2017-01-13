Feature: OcrTiffTesseractWebservice Acceptance

  Scenario: Successful Commit
   	  When OcrTiffTesseractWebservice CFT is launched
   	  Then I get CFT output
   	  Then I execute my RestFul service