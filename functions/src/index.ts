import {setGlobalOptions} from "firebase-functions";
import {HttpsError, onCall} from "firebase-functions/v2/https";
import * as logger from "firebase-functions/logger";
import {defineSecret} from "firebase-functions/params";
import {GoogleGenAI, ThinkingLevel} from "@google/genai";

const allowedUserUid = defineSecret("ALLOWED_USER_UID");
const geminiApiKey = defineSecret("GEMINI_API_KEY");

setGlobalOptions({
  region: "us-east4",
  maxInstances: 1,
});

export const askAiCoach = onCall(
  {
    secrets: [
      allowedUserUid,
      geminiApiKey,
    ],
  },
  async (request) => {
    if (!request.auth) {
      throw new HttpsError(
        "unauthenticated",
        "Sign in is required to use AI Coach.",
      );
    }
    if (request.auth.uid !== allowedUserUid.value()) {
      throw new HttpsError(
        "permission-denied",
        "You are not authorized to use AI Coach.",
      );
    }
    const message = request.data?.message;

    if (typeof message !== "string" || message.trim().length === 0) {
      throw new HttpsError(
        "invalid-argument",
        "A non-empty message is required.",
      );
    }
    logger.info("AI Coach request received");

    try {
      const ai = new GoogleGenAI({
        apiKey: geminiApiKey.value(),
      });

      const response = await ai.models.generateContent({
        model: "gemini-3.5-flash",
        contents: message,
        config: {
          systemInstruction: [
            "You are RoundTimer's encouraging AI Coach.",
            "Help users set realistic focus and rest intervals.",
            "Reply in 2–4 complete sentences.",
            "Use plain text only. Do not use Markdown formatting.",
            "Keep replies concise, practical, and supportive.",
          ].join(" "),
          thinkingConfig: {
            thinkingLevel: ThinkingLevel.MINIMAL,
          },
          maxOutputTokens: 400,
        },
      });
      const reply = response.text?.trim();
      if (!reply) {
        throw new HttpsError(
          "internal",
          "AI Coach did not return a response.",
        );
      }
      return {reply};
    } catch (error) {
      logger.error("Gemini request failed", error);

      if (error instanceof HttpsError) {
        throw error;
      }

      throw new HttpsError(
        "internal",
        "AI Coach is temporarily unavailable.",
      );
    }
  });
