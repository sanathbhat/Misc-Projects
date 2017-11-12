using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace WordamentSolver
{
    class Word
    {
        private string wordString;
        public string WordString
        {
            get { return wordString; }
            set { wordString = value; }
        }

        private int score;
        public int Score
        {
            get { return score; }
            set { score = value; }
        }

        public Word(string wordString, int score)
        {
            this.WordString = wordString;
            this.Score = score;
        }

        public override string ToString()
        {
            return wordString + "\t" + score;
        }

        public override bool Equals(object obj)
        {
            return wordString.Equals(((Word)obj).wordString);
        }

        public bool Equals(Word word)
        {
            return wordString.Equals(word.wordString);
        }

        public override int GetHashCode()
        {
            return wordString.GetHashCode();
        }
    }
}
