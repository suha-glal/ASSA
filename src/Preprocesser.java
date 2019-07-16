

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import weka.classifiers.functions.SMO;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Preprocesser {
	SubjInstance subjInst;
	PolarInstance polerInst;
	private SMO mySMOSubj;
	private SMO mySMOPola ;
	
	private NormlizeWord normalizer;
	
	
	private HashSet<String> negUniq;
	private HashSet<String> posUniq;
	private HashSet<String> namedEntities;
  
	private String garWords[]={"في","من","علي","عن","الي"};
	public HashSet<String>garLetters=new HashSet<String>(Arrays.asList(garWords));
  
	private ArrayList<String> namedEntity=FileReadToString.File2ArrString("C:/Users/Suha/workspace/WekaTest/lexicon/namedEntity.txt");
   
  
	private String[] postiveExpression={"ولا اروع","لا يعلا عليه","عقبال ما نفرح","بيض الله وجهك","لله دره","مقام محمود","ماجور ان شا","ولله حمد","في عيون كحل","كبير يا", "يشد من ازر","يحطم ارقام قياسيه","الرقم 1","الله محي اصله","حمد لله","ما عليه ملاحظ","ما فيها كلام"};

	private String[] NeutralExpression={"العكس صحيح","صباح النور","صباح الخير","يا قاتل يا مقتول","سلام","مساء الخير","مساء النور","سلامة","المهمة","يوسف","للكرة","الكرة","خاﻻتك","خالتي"};
		
	private String [] negativeExpression={"بئس القوم انتم","قبحگم الله","بلا قرف","قليل ذوق","قليل الذوق","شايف حاله","مسحوب عليه","اهفك بالنعال","قلة ادب","قلت ادب","فاقع كبدي","فاقعه كبدي","العن اوم","انت وجهك","فشيت خلق","ابو لهب","وكل تراب","ضف وجهك","كل تبن","الحلو مايكملش","سود الله وجهك","ملعوب عليه","فيها نظر","فيها كلام"};
	private String negativeWords[]={"سوء","وسوء","استهزاء","تججرحح","سخخيفههه","بلاخرابيط","اللعانه","هالبقاره","تافه","سئمت","فاجعة","معاناة","وحزين","اللعينة","للأسف","ونسوا","لسجين","ألغيت","وبئس","ابعدت","تفاهة","المشركين","لاسرائيل","الدخلاء","ياغبي","والشيوعي","خرافات","طائفية","اسرائيل","الاستيطان","الصهيوني","مستائه","عنصرياً","وإلهاب"};
	private String positiveWords[]={"البركة","الثناء","لأقوى","نعمه","نعمة","كيوت","والانسانيه","وعافيه","تهنئة","الولاء","وتقوية","ولائهم","وطنيين","مبارك","باركولي","الراحه","راحة","رووعه","انسانيه","متباركين","وشفاعه","ابارك"};

	private String [] spamwords ={"﻿أول","مطلوب","قريبا","ممتاز","الحديث","تبيع","موديل","اجهزة","جميع","آكثر","جميل","خرافية","سرعة","فرصة"
			,"سيقام","وشتري","تبدأ","نظيف","للإعلانات","جهاز","وسعره","تعلن","المواصفات","اعلان","وخصم","لأكثر","اكسسوراته","السعر","الشراء",
			"جديدة","واكثر","حصري","واربح","الاستفسار","حصرا","اللعبة","زبائنا","التفاصيل","برنامج","مسابقة","وسعرها","بمواصفات","القادم","المميزات"
			,"ابيع","تاجير","للأتصال","رائعه","رائعة","خاصة","خارقة","اسعار","تحميل","اشتري","اشترك","والجهاز","الممتازه","افضل","جديدة","جديده"
			,"البيع","للطلب","للتطبيقات","والسعر","واسعاره","الاعلان","بسعر","الاصدار","للبيع","البرامج","مجاني","مجانا","مجانيه","للاعلان","الآن",
			"راقي","التطبيق","المطلوب","تطبيق","ومطلوب","والحصري","المزايا","بشرى","بزيادة","سعرها","اكثر","عروض","برامج","الجديد","بنظام",
			"التطبيقات","موصفات","لأجهزة","مطلوبه","للإصدار","متوفر","الاشتراك","بيعه","تتميز","مطلووب","اربح","ولعبة","جميله","رائع","الخصائص",
			"اسرع","جميلة","حديث","الأولى","والاستفسار","وبسعر","نادرة","تطوير","للتأجير","خاص","محدودة","محدوده","سريعة","المجانية","أكبر",
			"انشر","انشرها","الحصري","خصم","اعلانات","العاب","ملحقاته","مبيعات","النسخه","والطلب","القادمة","الإعلان","الآصدار","موديل","باسعار",
			"مفاجئة","ممتازه","ممتازة","التجديد","أفضل","تقليد","سارع","بأحدث","الطلب","الشامل","والبيع","خصائص","بالاعلان","فائقة","ذكية",
			"ومواصفاته","وتطبيقات","فورا","واهم","مواصفات","السرعه","نضيفة","نضيفه","الأعلان","الافضل","يوقفه","السرعة","المطور","لسعر","تطبيقات"
			,"حصريا","لأحدث","خلفيات","الموديل","بيع","متصفح","للجادين","خرافي","افتتاح","بحالة","خلفية","ابشرروا","تقنية","للمغردين","انشرها",
			"خيالية","وسهل","الأفضل","مميز","مميزات","التحديث","نظيفه","نظيفة","لشراء","التميز","جديد ","العروض","المنتدى ","برعاية","تحديث",
			"عملاق","السعر","تنزيلات","إصدار","اجمل","الجديدة","احلى","الالعاب","إعلان","المميزة"};
	
	private ArrayList<String> Nots= new ArrayList<String>();
	
    Preprocesser() {
    	normalizer= new NormlizeWord();
    	subjInst= new SubjInstance();
    	polerInst= new PolarInstance();
    	try {
			prepareSubjectivityPolarityClassifier();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
  	   
 void preparefiles()throws  IOException {
    ArrayList<String> negative= FileReadToString.File2ArrString("C:/Users/Suha/workspace/WekaTest/lexicon/neg.txt");	 
    ArrayList<String> positive= FileReadToString.File2ArrString("C:/Users/Suha/workspace/WekaTest/lexicon/pos.txt");
    negUniq = new HashSet<String>(negative);
    posUniq = new HashSet<String>(positive);
    
	
	Nots.add("ليس");
	Nots.add("لن");
	Nots.add("لم");
	Nots.add("لا");
	Nots.add("مو");
	Nots.add("غير");
	Nots.add("لست");
	Nots.add("ماني");
	Nots.add("مش");
	Nots.add("ماحد");
	Nots.add("وليس");
	Nots.add("ولن");
	Nots.add("ولم");
	Nots.add("ولا");
	Nots.add("ومو");
	Nots.add("وغير");
	Nots.add("ولست");	
	Nots.add("وماني");
	Nots.add("ومش");
	Nots.add("وماحد");
	Nots.add("لايجوز");
    }   
  
	private String UsernameTag(String item) {
		String result = "";
		if(item.matches(".*@.*"))
			result =  "USERNAME";
		else
			result = item;
		return result;
	}
	
	 String TargetTag(String tweet, String target)
	{
		 tweet=tweet.replaceAll(target,  "target");
		 return tweet;
	}
		
	
	private String UrlTag(String item) {
		String result = "";
		if(item.matches(".*http://.*")||item.matches(".*www\\..*"))
			result = "URL";	
		else
			result = item;
		return result;
	}
	
	
	private String replaceEmoticons(String item) {
		String result = item;
		result = result.replaceAll(":[)]+", "HAPPY");
		result = result.replaceAll(";[)]+", "HAPPY");
		result = result.replaceAll(":-[)]+", "HAPPY");
		result = result.replaceAll(";-[)]+", "HAPPY");
		result = result.replaceAll(":d", "HAPPY");
		result = result.replaceAll(";d", "HAPPY");
		result = result.replaceAll("=[)]+", "HAPPY");
		result = result.replaceAll(":[D]+", "HAPPY");
		result = result.replaceAll("X[D]+", "HAPPY");
		result = result.replaceAll("\\^_\\^", "HAPPY");
		result = result.replaceAll("♥","HAPPY");
		result = result.replaceAll(":-D", "HAPPY");
		result = result.replaceAll(	"☺", "HAPPY");
		result = result.replaceAll("=[D]+","HAPPY");
		result = result.replaceAll(":[(]+", "SAD");
		result = result.replaceAll(":-[(]+", "SAD");
		result = result.replaceAll(":[|]+", "SAD");
		//result = result.replaceAll(":|", "SAD");
		result = result.replaceAll(":\\'[(]+","SAD");
		result = result.replaceAll(":-P","SAD");
		
		return result;
	}
		
	private String removeRepeatedCharacters(String item) {
		String result = item;
		result = item.replaceAll("(.)\\1{2,100}", "$1$1");//$1
		return result;
	}
	
	
	private String recognizeLaugh(String item) {
		String result = item;
		if(result.equals("كااك")||result.equals("kaak")||result.equals("ههه")|| result.equals("kak")||result.equals("hah")||result.equals("haah")||result.equals("lol")||result.equals("lool")||result.equals("هه")||result.equals("هاها")||result.equals("كاكا")||result.equals("خخ")||result.equals("هع"))
			result = "LAUGH";
		
		return result;
	}
	
	
	 void SpamAttExt(String str, SpamAttribute ra)
	{ 
		 List<String> spamwordList = Arrays.asList(spamwords);  
	int url=0,hashtag=0,username=0,rt=0, spamword=0, noNum=0, nodigits=0;

		
		String []tokens=str.split("\\s");
		int noofWords=tokens.length;
		for(String token:tokens)
		{
			
			if(token.equalsIgnoreCase("URL"))
			    url++;
			if(token.equalsIgnoreCase("HASHTAG"))
			    hashtag++;
		    if(token.equalsIgnoreCase("RT"))
		    	rt++;
			if(token.equalsIgnoreCase("USERNAME"))
				username++;
			if(spamwordList.indexOf(token)!=-1)
				spamword++;
			
			
			if(token.matches("(-|\\+)?[0-9]+(\\.[0-9]+)?"))

			{
				noNum++;
				nodigits=nodigits+token.length();
			}
			
			
			
			
		}
		
		
		ra.attValues[0]=hashtag;
		ra.attValues[1]=url;
		ra.attValues[2]=username;
		ra.attValues[3]=rt;
		ra.attValues[4]=nodigits;
		ra.attValues[5]=noNum;
		ra.attValues[6]=noofWords;
		ra.attValues[7]=spamword;
		ra.attValues[8]=hashtag/(double)noofWords;
		ra.attValues[9]=url/(double)noofWords;
		
	}
	
	 
	
	 void SubjectiveAttFreq(String tweet,String orgtweet,SubjAttrFreq att)
	 {
			
			att.AttributeValues[0]=noOccurance("Pos",tweet);//no of pos words
			att.AttributeValues[1]=noOccurance("Neg",tweet);//no of neg words
			att.AttributeValues[2]=countPunc(orgtweet);//no of punctions marks
			att.AttributeValues[3]=noOccurance("USERNAME",tweet);//no of usernames
			att.AttributeValues[4]=countQues(orgtweet);//no of question marks
			double p=ShortedDistancebetween1("target","Pos",tweet);//
			double n=ShortedDistancebetween1("target","Neg",tweet);
			att.AttributeValues[5]=p;//distance between the positive word and the target
			att.AttributeValues[6]=n;//distance between the negative word and the target
			att.AttributeValues[7]=(p<n?1:0);//near pos
			att.AttributeValues[8]=(p>n?1:0);//near neg
			
	 
 }
	
	 void PolarityAttFreq(String tweet,String orgtweet,PolarAttrFreq att)
	 {
		
			att.AttributeValues[0]=noOccurance("Pos",tweet);//no of pos words
			att.AttributeValues[1]=noOccurance("Neg",tweet);//no of neg words
			att.AttributeValues[2]=countQues(orgtweet);//no of question marks
			double p=ShortedDistancebetween1("target","Pos",tweet);//
			double n=ShortedDistancebetween1("target","Neg",tweet);
			att.AttributeValues[3]=p;//distance between the positive word and the target
			att.AttributeValues[4]=n;//distance between the negative word and the target
			att.AttributeValues[5]=(p<n?1:0);//near pos
			att.AttributeValues[6]=(p>n?1:0);//near neg
			
	 
 }
	
	 private double ShortedDistancebetween1(String word1,String word2,String sentence)
	 {
		 int shortd=1000;
		 Pattern p = Pattern.compile(word1);
		    Matcher m = p.matcher(sentence);
		   int indx1=0;
		   int indx2=0;
		   
				 while (m.find())
				 {
					 indx1=m.start();
					
					 Pattern p2 = Pattern.compile(word2);
					    Matcher m2 = p2.matcher(sentence);
					    while(m2.find())
					    {
					    	indx2=m2.start();
					    	int dis=Math.abs(indx1-indx2);
					    	
					    	if(shortd>dis)
					    		shortd=dis;
					    }
				 }
				 
		 return shortd;
	 }
	 

	 private  int countPunc(String s)
	 { String punc="“!@#$%^&*()؛<>|.؟,’~/?\'\";:";
		 int count=0;
		 for(int i=0;i<s.length();i++)
		 {
			 if(punc.indexOf(s.charAt(i))!=-1)
			 count++;
		 }
		 return count;
	 }
	
	 private  int countQues(String s)
	 { String punc="?؟";
		 int count=0;
		 for(int i=0;i<s.length();i++)
		 {
			 if(punc.indexOf(s.charAt(i))!=-1)
			 count++;
		 }
		 return count;
	 }
	
	 private String variationsReplacment(String word)
	{
		if(word.startsWith("ء"))
		word=word.replaceAll("ء"	,"ا");
		word=word.replaceAll("ء","ا");
		word=word.replaceAll("آ","ا");
		word=word.replaceAll("أ"	,"ا");
		word=word.replaceAll("إ"	,"ا");
		word=word.replaceAll("ة"	,"ه");
		word=word.replaceAll("ئ"	,"ي");
		word=word.replaceAll("ىء","ي");
		word=word.replaceAll("ى"	,"ي");
		word=word.replaceAll("ـ"	,"");
		word=word.replaceAll("ٱ"	,"ا");
		word=word.replaceAll("ٶ"	,"و");
		word=word.replaceAll("ؤ"	,"و");
		
		
		return word;
	}
		
	 private String NamedEntityTag(String word)
	{
		if(namedEntities.contains(word))
			return "NEntity";
		else 
			return word;
	}
	
	 private boolean isPositive(String word)
	{
		if(posUniq.contains(word))
			return true;
		else 
			return false;
	}
	
	 private boolean isNegative(String word)
	{
		if(negUniq.contains(word))
			return true;
		else 
			return false;
	}
	
	
	
	 private  String Normalization (String word)
	{
		word = word.replaceAll("(.)\\1{2,100}", "$1$1");//repalce repaeted char with 2
	//	word = word.replaceAll("(.)\\1{1,100}", "$1");//replace repeated char with 1 copy
		word=variationsReplacment(word);
		word=normalizer.normlizeWord(word);
		return word;
	}
	
	
	 private  String lettersRepalcement(String tobereplaced)
	{
		String replaced= tobereplaced.replaceAll("ھ", "ه");
		replaced=replaced.replaceAll("ﻟ", "ل");
		replaced=replaced.replaceAll("ۿ", "ه");
		replaced=replaced.replaceAll("گ", "ك");
		replaced=replaced.replaceAll("ﻻ", "لا");
		return replaced;
	}
	
	public String []preprocessTweetWord(String []words) throws IOException  {
		
		
		
		
		String newWord[]= new String[words.length];
		
		for(int i=0;i<words.length;i++)
		{
				
			newWord[i]= words[i].replaceAll("#!?","");
			newWord[i]=lettersRepalcement(newWord[i]);
			
			newWord[i] = removeRepeatedCharacters(newWord[i]);	 
				
			newWord[i]=UrlTag(newWord[i]);
		    
			newWord[i]=UsernameTag(newWord[i]);
			
			newWord[i]=replaceEmoticons(newWord[i]);
			newWord[i]=newWord[i].replaceAll("[^a-zA-Zء-غ0-9ف-ي]+","");
			newWord[i]=DetectPosWord(newWord[i]);
			newWord[i]=DetectNegWord(newWord[i]);
						
			newWord[i]=Normalization(newWord[i]);
			newWord[i]=RecognizeNot(newWord[i]);
			newWord[i]=recognizeLaugh(newWord[i]);
			
						
		}
		return newWord;
}
		

	private String DetectPosWord(String word)
{
		String detected=word;
for(int i=0;i<positiveWords.length;i++)
	{
		if(word.equals(positiveWords[i]))
		{
	detected="PosW";
	break;
		}
	}//for
return detected;
}

	private String DetectNegWord(String word)
{
 String detected=word;
for(int i=0;i<negativeWords.length;i++)
{
	if(word.equals(negativeWords[i]))
	{
detected="NegW";
break;
	}
}//for
return detected;
}

	private String RecognizeNot( String str )
{
	
	if(Nots.contains(str))
		str="nnoott";
	
	return str;
	
}


	public String DetectingPosNegExpressions(String preoccesed)
{
	String expstr=preoccesed;
	
	
	
	String [] expwords=expstr.split("\\s");
	
	for(int i=0;i<expwords.length-1;i++)
	{
	if (expwords[i].equals("nnoott"))
		{
			if(expwords[i+1].contains("PosW"))
				{
				expwords[i]="";
				expwords[i+1]="NegE";
				}
			else if(expwords[i+1].contains("NegW"))
				{
				expwords[i]="";
				expwords[i+1]="PosE";			
				}
			else if(!expwords[i+1].contains("target") && !expwords[i+1].contains("nentity") && expwords[i+1].length()>2)
			{
				expwords[i]="";
				expwords[i+1]="NegE";
			}
			
		}//nnoott
		if(expwords[i].contains("PosW") && expwords[i+1].contains("Neg")||
				expwords[i].contains("Neg") && expwords[i+1].contains("PosW"))	
		{
			expwords[i]="";
			expwords[i+1]="NegE";
		}
		
		if(expwords[i].equals("وابغ")||expwords[i].equals("نحتاج"))
		{
			if(expwords[i+1].contains("PosW"))
			{
			expwords[i]="";
			expwords[i+1]="NegE";
			}
		
		}
		if(expwords[i].equals("وش")||expwords[i].equals("شو"))
		{
			if(expwords[i+1].contains("PosW")||expwords[i+1].contains("NegW"))
			{
			expwords[i]="";
			expwords[i+1]="NeutE";
			}
		
		}
		
	}//for
	
	
	StringBuilder builder = new StringBuilder();
	for(String s : expwords) {
		if (!(s.equals(" ")||s.equals("")))
	    builder.append(s+" ");
	}
	expstr=builder.toString();
	return expstr;
	

}
	
public String DetectNeutralPosNegExpression(String tweet)
{
	String expstr=tweet;
	
	for(int i=0;i<NeutralExpression.length;i++)
	{
		
		expstr=expstr.replaceAll(NeutralExpression[i], "NeutE");
		
	}

	
	for(int i=0;i<postiveExpression.length;i++)
	{
		
			expstr=expstr.replaceAll(postiveExpression[i], "PosE");
	}//for
	
	for(int i=0;i<negativeExpression.length;i++)
	{
			expstr=expstr.replaceAll(negativeExpression[i], "NegE");
	}//for
	
	expstr=expstr.replaceAll("بلا target", "NegE target");
	return expstr;
	}

public String DetectNamedEntity(String tweet)
{
	String expstr=tweet;
	
	for(int i=0;i<namedEntity.size();i++)
	{
		expstr=expstr.replaceAll(namedEntity.get(i), " NEntity ");
		
	}


	return expstr;
	}

public String []DetectingPosNegTweetWords(String tweetwords[])
{
	String newWord[]= tweetwords;
	
	for(int i=0;i<tweetwords.length;i++)
	{
	
		if(isPositive(tweetwords[i]))
		{
			newWord[i]=newWord[i]+"PosW";
		}
		else if(isNegative(tweetwords[i]))
		{
			newWord[i]=newWord[i]+"NegW";
		}
		
		
		
	}//for
	
	return newWord;
}

	private double noOccurance(String word, String sentence)
	{
		
	    Pattern p = Pattern.compile(word);
	    Matcher m = p.matcher(sentence);
	    double count = 0;
	    while (m.find()){
	    	count +=1;
	    }
	    return count;
	}
public String preprocessTweet(String item) throws IOException  {
		
		
		
		String result_fin = "";
		String result = item;
		
		//make the sentence lower case
	   result=result.toLowerCase();	
		//remove retweets
	  // result=result.replaceAll("rt", "RT");
	   result= result.replaceAll("#", " hashtag ");
		//remove repeated charachters
	   result = removeRepeatedCharacters(result);
		
	   //adding Arabic words subject tag 
	 //   result=TargetTag(result);
		
	    
		String[]words=result.split("\\s");
		
		for(String word:words)
		{
		
		    word=UrlTag(word);
			word=UsernameTag(word);
			word=NamedEntityTag(word);
			word=replaceEmoticons(word);
			word=recognizeLaugh(word);
		//word=word.replaceAll("[^a-zA-Zء-غ0-9ف-ي]+","");//for arabic and english
		//word=word.replaceAll("[^a-zء-غA-Zف-ي]+","");//for arabic and english
		 //$ word=word.replaceAll("[^ء-ي]+","");//for arabic
		
		word=Normalization(word);
		
	
		/*  if(word.matches("^[ء-ي]+$"))
		  {
		   		   word=stemmer.StemingWord(word);
		   		 //  System.out.println(word);
		  }*/
			 
		  
			if(word.length()>0 )//&& isValidArabicWord(word)
			result_fin = result_fin +" "+word;
		}
		
	
		return result_fin.toLowerCase();
		
	}
	public void prepareSubjectivityPolarityClassifier() throws Exception
	{
		
		
		
		mySMOSubj= new SMO();
		mySMOSubj.setOptions(weka.core.Utils.splitOptions("-C 1.0 -L 0.0010 -P 1.0E-12 -N 0 -V -1 -W 1 -K \"weka.classifiers.functions.supportVector.PolyKernel -C 250007 -E 1.0\""));    
		String subjdatasetpath="C:/Users/Suha/workspace/WekaTest/ARFF/AllfiveSelectedAttSubj.arff";
		DataSource subjsource = new DataSource(subjdatasetpath);
	    Instances subjdata = subjsource.getDataSet();
		subjdata.setClassIndex(subjdata.numAttributes() - 1);		
		mySMOSubj.buildClassifier(subjdata);
		
		 
		mySMOPola=new SMO();
		mySMOSubj.setOptions(weka.core.Utils.splitOptions("-C 1.0 -L 0.0010 -P 1.0E-12 -N 0 -V -1 -W 1 -K \"weka.classifiers.functions.supportVector.PolyKernel -C 250007 -E 1.0\""));
			
		String polrdatasetpath="C:/Users/Suha/workspace/WekaTest/ARFF/AllfiveSelectedAttPolar.arff";
		DataSource polrsource = new DataSource(polrdatasetpath);
	    Instances polrdata = polrsource.getDataSet();
		polrdata.setClassIndex(polrdata.numAttributes() - 1);		 
		 mySMOPola.buildClassifier(polrdata);

 
		 
	}
	public int PredictPolarity(Instances inst) 
	{
		double pred=-1;
		try {
			pred = mySMOPola.classifyInstance(inst.firstInstance());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			 
    return (int) pred;
	}//predict
	public int PredictSubjectivity(Instances inst) 
	{		
		double pred=-1;
		try {
			pred = mySMOSubj.classifyInstance(inst.firstInstance());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
					   
    return (int) pred;
	}//predict
}//class
