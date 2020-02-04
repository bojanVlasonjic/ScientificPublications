xquery version "3.0";
declare namespace sp = "http://www.ftn.uns.ac.rs/scientific-publication";
for $ref in doc("/db/scientific-publication/scientific-papers/e2859775-cf5d-4bc7-9181-6596ef658ffa")/sp:scientific-paper/sp:references/sp:reference
let $href := data($ref/@href)
return $href