using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using NetSpell.SpellChecker.Dictionary;
using NetSpell.SpellChecker;
using System.Collections;
using System.Threading;
using System.IO;

namespace WordamentSolver
{
    public partial class Form1 : Form
    {
        WordDictionary dict = new WordDictionary();
        Spelling spellChecker = new Spelling();

        int minWordSize = 3;
        int maxWordSize = 16;

        HashSet<Word> lstAllWords = new HashSet<Word>();

        HashSet<string> allSubstringsDictionary = new HashSet<string>();

        string[] tiles = new string[16];
        int[] scores = new int[16] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

        int[][] neighbours = {
                                    new int[] {1, 4, 5},
                                    new int[] {0, 2, 4, 5, 6},
                                    new int[] {1, 3, 5, 6, 7},
                                    new int[] {2, 6, 7},

                                    new int[] {0, 1, 5, 8, 9},
                                    new int[] {0, 1, 2, 4, 6, 8, 9, 10},
                                    new int[] {1, 2, 3, 5, 7, 9, 10, 11},
                                    new int[] {2, 3, 6, 10, 11},

                                    new int[] {4, 5, 9, 12, 13},
                                    new int[] {4, 5, 6, 8, 10, 12, 13, 14},
                                    new int[] {5, 6, 7, 9, 11, 13, 14, 15},
                                    new int[] {6, 7, 10, 14, 15},

                                    new int[] {8, 9, 13},
                                    new int[] {8, 9, 10, 12, 14},
                                    new int[] {9, 10, 11, 13, 15},
                                    new int[] {10, 11, 14}
                               };
        public Form1()
        {
            InitializeComponent();
            dict.DictionaryFile = "en-US.dic";
            dict.Initialize();
            spellChecker.Dictionary = dict;

            //FileStream fs = new FileStream("Dictionary.txt", FileMode.Open , FileAccess.Read);
        }

        private void btnSolve_Click(object sender, EventArgs e)
        {
            lstAllWords = new HashSet<Word>();
            for (int i = 0; i < 16; i++)
            {
                tiles[i] = pnlBoard.Controls["txt" + i].Text;
                string tileScore = pnlScores.Controls["txtScore" + i].Text;
                scores[i] = Convert.ToInt32(tileScore!=""?tileScore:"1");
            }

            //starting with each tile inturn
            for (int i = 0; i < 16; i++)
            {
                string currentString = "";
                int currentScore = 0;
                ArrayList usedTiles = new ArrayList();
                CheckIfWordInDictionary(currentString, currentScore, i, usedTiles, 0);
            }

            lstbWords.DataSource = null;
            lstbWords.DataSource = lstAllWords.OrderByDescending(x => x.WordString.Length).ToList<Word>();
            lblTotal.Text = "Total Found = " + lstAllWords.Count;
        }

        private void CheckIfWordInDictionary(string currentString, int currentScore, int toBeAddedTile,
            ArrayList usedTiles, int sizeYet)
        {
            if (usedTiles.Contains(toBeAddedTile))
                return;

            currentString += tiles[toBeAddedTile];
            currentScore += scores[toBeAddedTile];

            if (++sizeYet >= minWordSize && spellChecker.TestWord(currentString))
            {
                //txtWords.AppendText(currentString + "\n");
                //save in list to prevent duplicates
                lstAllWords.Add(new Word(currentString, currentScore));
                //if (lstAllWords.Count % 100 == 0)
                //{
                //    lstbWords.DataSource = lstAllWords;
                //}
            }

            if (sizeYet + 1 > maxWordSize)
                return;

            usedTiles.Add(toBeAddedTile);
            foreach (int neighbour in neighbours[toBeAddedTile])
            {
                //check if there is hope for new word with current substring
                if (allSubstringsDictionary.Contains(currentString))
                    CheckIfWordInDictionary(currentString, currentScore, neighbour, usedTiles, sizeYet);
            }
            usedTiles.Remove(toBeAddedTile);
        }

        private void btnClear_Click(object sender, EventArgs e)
        {
            //txtWords.Clear();
            for (int i = 0; i < 16; i++)
            {
                pnlBoard.Controls["txt" + i].Text = "";
                pnlScores.Controls["txtScore" + i].Text = "";
            }

            scores = new int[16] { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
            txt0.Focus();
            lstbWords.DataSource = null;
            lblTotal.Text = "";
            progressBar1.Value = 0;
        }

        #region focus change
        //changing focus to next textbox
        private void txt0_TextChanged(object sender, EventArgs e)
        {
            txt1.Focus();
        }

        private void txt1_TextChanged(object sender, EventArgs e)
        {
            txt2.Focus();
        }

        private void txt2_TextChanged(object sender, EventArgs e)
        {
            txt3.Focus();
        }

        private void txt3_TextChanged(object sender, EventArgs e)
        {
            txt4.Focus();
        }

        private void txt4_TextChanged(object sender, EventArgs e)
        {
            txt5.Focus();
        }

        private void txt5_TextChanged(object sender, EventArgs e)
        {
            txt6.Focus();
        }

        private void txt6_TextChanged(object sender, EventArgs e)
        {
            txt7.Focus();
        }

        private void txt7_TextChanged(object sender, EventArgs e)
        {
            txt8.Focus();
        }

        private void txt8_TextChanged(object sender, EventArgs e)
        {
            txt9.Focus();
        }

        private void txt9_TextChanged(object sender, EventArgs e)
        {
            txt10.Focus();
        }

        private void txt10_TextChanged(object sender, EventArgs e)
        {
            txt11.Focus();
        }

        private void txt11_TextChanged(object sender, EventArgs e)
        {
            txt12.Focus();
        }

        private void txt12_TextChanged(object sender, EventArgs e)
        {
            txt13.Focus();
        }

        private void txt13_TextChanged(object sender, EventArgs e)
        {
            txt14.Focus();
        }

        private void txt14_TextChanged(object sender, EventArgs e)
        {
            txt15.Focus();
        }

        private void txt15_TextChanged(object sender, EventArgs e)
        {
            btnSolve.Focus();
        }
        #endregion

        private void btnSetup_Click(object sender, EventArgs e)
        {
            //setting up the all substrings dictionary
            try
            {
                List<string> allWords = File.ReadAllText("Dictionary.txt").
                Split(new char[] { '\n' }).ToList();

                //MessageBox.Show(allWords.Count+"");
                //int totalWordCount = 235883;
                int batchWordsDone = 0;

                foreach (string word in allWords)
                {
                    if (word.Length > 2)
                    {
                        for (int length = 1; length <= word.Length; length++)
                        {
                            string subString = word.Substring(0, length);
                            //if (!allSubstringsDictionary.Contains(subString))
                            //{
                            allSubstringsDictionary.Add(subString);
                            //}
                        }
                    }
                    batchWordsDone++;
                    if (batchWordsDone == 2358)
                    {
                        progressBar1.PerformStep();
                        batchWordsDone = 0;
                    }
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void btnScore_Click(object sender, EventArgs e)
        {
            lstbWords.DataSource = null;
            lstbWords.DataSource = lstAllWords.OrderByDescending(x => x.Score).ToList<Word>();
        }
    }
}
