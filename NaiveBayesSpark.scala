//import
import org.apache.spark.mllib.classification.{NaiveBayes, NaiveBayesModel}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.linalg.{ SparseVector => SV }
import org.apache.spark.mllib.feature.HashingTF
import org.apache.spark.mllib.feature.IDF
import org.apache.spark.mllib.evaluation.MulticlassMetrics

//input file
// val data = sc.textFile("/Volumes/DATA/Downloads/output.txt")
val data = sc.textFile("output.txt")

//label
val datalabel = data.map { line =>
  val parts = line.split(',').map(_.toLowerCase)
  if(parts(14)=="open"){parts(14)="0"}else{parts(14)="1"}
  parts(14)
}
val openOrClose=datalabel.distinct.collect().zipWithIndex.toMap



//TF-IDF
val databody = data.map { line =>
  val parts = line.split(',').map(_.toLowerCase)
  parts(6)+" "+parts(7)+" "+parts(8)+" "+parts(9)+" "+parts(10)+" "+parts(11)+" "+parts(12)
}

//token
def tokenize(input: String): Seq[String] = {
    val stopwords = Set("the","a","an","of","or","in","for","by","on","but", "is", "not", "with", "as", "was", "if", "they", "are", "this", "and", "it", "have", "from", "at", "my", "be", "that", "to", "you", "am", "what", "do", "any", "there", "when", "so", "some", "which", "me", "would", "should", "where", "has", "will", "here", "then", "we")
    return input.split("""\W+""")
    .map(_.toLowerCase)
    .filterNot(token => stopwords.contains(token))
    .filter(token => token.size >= 2)
    .toSeq
}
val para = math.pow(2, 22).toInt
val hash = new HashingTF(para)
val bodytoken=databody.map{content => hash.transform(tokenize(content))}
val finalbody=new IDF().fit(bodytoken).transform(bodytoken)

//train
val zipped=datalabel.zip(finalbody)
val train=zipped.map{case(label, vector) => LabeledPoint(openOrClose(label), vector)}

val splits = train.randomSplit(Array(0.6, 0.4), seed = 11L)
val training = splits(0)
val test = splits(1)

val model = NaiveBayes.train(training, lambda = 1.0)

val predictionAndLabel = test.map(p => (model.predict(p.features), p.label))
val accuracy = 1.0 * predictionAndLabel.filter(x => x._1 == x._2).count() / test.count()
