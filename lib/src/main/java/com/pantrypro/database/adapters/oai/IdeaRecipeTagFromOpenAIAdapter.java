//package com.pantrypro.database.adapters.oai;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class IdeaRecipeTagFromOpenAIAdapter {
//
//    public static List<IdeaRecipeTag> getIdeaRecipeTags(OAIGPTFunctionCallResponseTagRecipeIdea oaiFunctionCallResponse, Integer ideaID) {
//        List<IdeaRecipeTag> tags = new ArrayList<>();
//
//        for (String tag: oaiFunctionCallResponse.getTags()) {
//            tags.add(new IdeaRecipeTag(
//                    ideaID,
//                    tag
//            ));
//        }
//
//        return tags;
//    }
//
//}
