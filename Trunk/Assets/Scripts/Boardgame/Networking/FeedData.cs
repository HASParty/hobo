﻿using Boardgame.GDL;
using System;
using System.Collections.Generic;
using System.Linq;
using UnityEngine;

namespace Boardgame.Networking {
    public class FeedData {
        public Move Best;
        public Dictionary<Move, FMove> Moves = new Dictionary<Move, FMove>();
        public float SimulationStdDev;
        public double AverageSimulations;
        public int TotalSimulations;
        public float FirstWeightedUCT;
        public float SecondWeightedUCT;

        public FeedData(string parse) {
            var moves = BoardgameManager.Instance.reader.GetConsideredMoves(parse);
            int maxsim = moves.Max(v => v.Simulations);
            TotalSimulations = moves.Sum(v => v.Simulations) + maxsim;
            FirstWeightedUCT = 0;
            SecondWeightedUCT = 0;
            foreach (var cm in moves) {
                if (cm.Simulations == maxsim) {
                    Best = (cm.First != null ? cm.First : cm.Second);
                }
                FMove m;
                m.Move = (cm.First != null ? cm.First : cm.Second);
                m.FirstUCT = cm.FirstUCT;
                m.SecondUCT = cm.SecondUCT;
                m.Who = (cm.First != null ? Player.First : Player.Second);
                m.Simulations = cm.Simulations;
                if (TotalSimulations > 0) {
                    int sim = (cm.Simulations == maxsim ? maxsim * 2 : cm.Simulations);
                    float weight = (float)sim / (float)TotalSimulations;
                    FirstWeightedUCT += m.FirstUCT * weight;
                    SecondWeightedUCT += m.SecondUCT * weight;
                }
                Moves.Add(m.Move, m);
            }

            try {
                AverageSimulations = moves.Average(v => v.Simulations);
                double stdDev = Math.Sqrt(moves.Average(v => Math.Pow(v.Simulations - AverageSimulations, 2)));
                SimulationStdDev = (maxsim - (float)AverageSimulations) / (float)stdDev;
            } catch (Exception e) {
                Debug.LogWarning(e);
            }
        }
    }
}
