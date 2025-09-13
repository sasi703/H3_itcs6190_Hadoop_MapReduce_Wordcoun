# Hadoop MapReduce Word Count

## 📌 Project Overview
This project implements a **Word Count program using Hadoop MapReduce in Java**.  
The program counts the occurrences of each word in a given text file, filters out words shorter than 3 characters, normalizes text to lowercase, and outputs the results sorted in descending order of frequency.

---

## ⚙️ Approach & Implementation
- **Mapper (WordMapper.java)**  
  - Converts each line to lowercase.  
  - Splits text into tokens using non-word characters (`\W+`).  
  - Emits `(word, 1)` for words of length ≥ 3.  

- **Reducer (WordReducer.java)**  
  - Aggregates counts for each word.  
  - Sorts results in descending order by frequency.  
  - Outputs `(word, total_count)`.  

- **Driver (Controller.java)**  
  - Configures the job.  
  - Sets Mapper, Combiner, and Reducer classes.  
  - Defines input and output paths.

---

## 🚀 Execution Steps
1. **Start Hadoop Cluster (Docker)**  
```
docker-compose up -d
```

2. **Upload Dataset**  
```
docker cp input.txt namenode:/input.txt
docker exec -it namenode bash
hdfs dfs -mkdir -p /input
hdfs dfs -put /input.txt /input/
```

3. **Run MapReduce Job**  
```
docker cp target/wordcount.jar namenode:/wordcount.jar
docker exec -it namenode bash
hdfs dfs -rm -r /output   # remove old output if exists
hadoop jar /wordcount.jar com.example.controller.Controller /input /output
```

4. **View Results**  
```
hdfs dfs -cat /output/part-r-00000
```

5. **Copy Results to Local**  
```
hdfs dfs -get /output /output_local
exit
docker cp namenode:/output_local ./output_local
```

---

## 📝 Example Input and Output
### Input (`input.txt`)
```
Cloud computing for data analysis
Cloud computing is powerful
Hadoop MapReduce for data processing
Big data analysis with Hadoop
```

### Output (`part-r-00000`)
```
data        3
cloud       2
analysis    2
computing   2
hadoop      2
for         2
mapreduce   1
powerful    1
processing  1
big         1
with        1
```

---

## ⚠️ Challenges & Solutions
- **Mixed case words (Cloud vs cloud)** → Normalized input to lowercase.  
- **Short words (is, to, of)** → Filtered out words with length < 3.  
- **Existing `/output` folder caused errors** → Removed using `hdfs dfs -rm -r /output`.  
- **Copying files between host and container** → Used `docker cp` to move files.  

---

## 📂 Repository Structure
```
H3_itcs6190_Hadoop_MapReduce_Wordcount/
├── src/                # Mapper, Reducer, Controller code
├── input.txt           # Input dataset
├── output_local/       # Job output (from HDFS)
├── README.md           # Project report
└── pom.xml             # Maven configuration
```
