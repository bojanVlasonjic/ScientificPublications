xquery version "3.0";
declare namespace sp = "http://www.ftn.uns.ac.rs/scientific-publications";

for $reference in doc("%s")/sp:scientific-paper/sp:references/sp:reference
return $reference

    