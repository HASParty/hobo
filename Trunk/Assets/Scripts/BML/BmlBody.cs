using System;
using System.Collections.Generic;
using UnityEngine;
namespace Behaviour
{
	/// <summary>
	/// Groups BML behaviours that sync with one another / are generated from a FML chunk
	/// </summary>
	public class BmlBody
	{
		public Dictionary<string, BmlChunk> Chunks { get { return chunks; } }
		Dictionary<string, BmlChunk> chunks = new Dictionary<string, BmlChunk>();

		public bool isDone = false;
		public bool syncComplete = false;

		public float latestEnd = 0f;



		public Speech SpeechChunk { get; private set; }

		BmlBody executeAfter;
		BmlBody executeWith;

		public bool IsReady() {
			return executeAfter == null || executeAfter.isDone;
		}

		public bool Synchronized() {
			return executeWith == null || executeWith.SpeechChunk == null || executeWith.syncComplete;
		}

		public bool NeedsSync() {
			return executeWith != null;
		}

		public Dictionary<string, BmlChunk> GetSyncChunks() {
			if (executeWith != null) {
				return executeWith.Chunks;
			} else {
				return null;
			}
		}

		public void ExecuteAfter(BmlBody body) {
			executeAfter = body;
		}

		public void ExecuteWith (BmlBody body)
		{
			executeWith = body;
		}

		public void AddChunk(BmlChunk chunk) {
			if (chunk.Type == BmlChunkType.Speech)
				SpeechChunk = chunk as Speech;
			else {
				if(chunks.ContainsKey(chunk.ID)) return;
				chunks.Add (chunk.ID, chunk);
				latestEnd = Math.Max (latestEnd, chunk.End); 
			}
		}
	}
}
