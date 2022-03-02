/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.ws;

import com.elm.shj.applicant.portal.services.chat.ChatContactService;
import com.elm.shj.applicant.portal.services.chat.ChatMessageService;
import com.elm.shj.applicant.portal.services.dto.ChatContactLiteDto;
import com.elm.shj.applicant.portal.services.dto.ChatMessageDto;
import com.elm.shj.applicant.portal.services.dto.ChatMessageLiteDto;
import com.elm.shj.applicant.portal.services.dto.CompanyStaffLiteDto;
import com.elm.shj.applicant.portal.services.integration.WsResponse;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for exposing web services related to chat contacts for external party.
 *
 * @author Slim Ben Hadj
 * @since 1.0.0
 */
@CrossOrigin(
        originPatterns = "*",
        maxAge = 3600,
        exposedHeaders = {"Authorization", JwtTokenService.CALLER_TYPE_HEADER_NAME, JwtTokenService.JWT_HEADER_NAME},
        allowCredentials = "true"
)
@Slf4j
@RestController
@RequestMapping(Navigation.API_INTEGRATION_CHAT_CONTACTS)
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ChatContactWsController {

    private final ChatContactService chatContactService;
    private final ChatMessageService chatMessageService;

    /**
     * List all chat contacts of a specific applicant by ritual ID.
     *
     * @param ritualId       the selected ritual ID
     * @param authentication the authenticated user
     * @return the list of chat contacts
     */
    @GetMapping("/list/{ritualId}")
    public ResponseEntity<WsResponse<?>> findChatContactsByUinAndRitualId(@PathVariable Long ritualId, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(chatContactService.findChatContactsByUinAndRitualId(loggedInUserUin, ritualId)).build());
    }

    /**
     * Creates a new chat contact of type applicant
     *
     * @return savedContact saved one
     */
    @PostMapping(value = "/create/{ritualId}")
    public ResponseEntity<WsResponse<?>> createApplicant(@PathVariable Long ritualId,
                                                         @RequestBody ChatContactLiteDto applicantChatContact,
                                                         Authentication authentication) {
        WsResponse response = chatContactService.createApplicantChatContact(ritualId, applicantChatContact);
        return ResponseEntity.ok(
                WsResponse.builder().status(response.getStatus())
                        .body(response.getBody()).build());
    }

    /**
     * Creates a new chat contact of type applicant
     *
     * @return savedContact saved one
     */
    @GetMapping(value = "/find/{applicantUin}/{contactUin}")
    public ResponseEntity<WsResponse<?>> findApplicantChatByApplicantUinAndContactUin(@PathVariable String applicantUin,
                                                                                      @PathVariable String contactUin,
                                                                                      Authentication authentication) {
        ChatContactLiteDto ChatContactLiteDto = chatContactService.findApplicantChatByApplicantUinAndContactUin(applicantUin, contactUin);
        if (ChatContactLiteDto.getContactDigitalId() == null)
            return ResponseEntity.ok(
                    WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                            .body(WsError.builder().error(WsError.EWsError.APPLICANT_CHAT_CONTACT_NOT_FOUND.getCode()).build()).build());
        return ResponseEntity.ok(WsResponse
                .builder()
                .status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                .body(ChatContactLiteDto).build());
    }


    @PostMapping(value = "/create-staff/{applicantRitualId}/{contactUin}")
    public ResponseEntity<WsResponse<?>> createStaff(@PathVariable Long applicantRitualId,
                                                     @PathVariable String contactUin,
                                                     Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        WsResponse response = chatContactService.createStaffChatContact(loggedInUserUin, applicantRitualId, contactUin);

        return ResponseEntity.ok(WsResponse
                .builder()
                .status(response.getStatus())
                .body(response.getBody()).build());
    }


    /**
     * Updates user defined chat contact of type applicant
     *
     * @param id                   the ID number of the chat contact to be updated
     * @param applicantChatContact the chat contact applicant
     * @param authentication       the authenticated user
     * @return updatedContact updatedOne one
     */
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<WsResponse<?>> update(@PathVariable long id,
                                                @RequestBody ChatContactLiteDto applicantChatContact,
                                                Authentication authentication) {
        return ResponseEntity.ok(WsResponse
                .builder()
                .status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                .body(chatContactService.updateApplicantChatContact(id, applicantChatContact)).build());
    }

    /**
     * delete user defined chat contact
     *
     * @return number of rows affected
     */
    @PostMapping("/delete/{applicantUin}/{contactUin}")
    public ResponseEntity<WsResponse<?>> deleteApplicantChatContact(@PathVariable String applicantUin, @PathVariable String contactUin) {
        return ResponseEntity.ok(WsResponse
                .builder()
                .status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                .body(chatContactService.deleteChatContact(applicantUin, contactUin)).build());
    }

    @GetMapping("/find-staff/{suin}")
    public ResponseEntity<WsResponse<?>> findOneApplicantByUinAndRitualId(@PathVariable String suin) {
        CompanyStaffLiteDto companyStaffLiteDto = chatContactService.findStaffContactBySuinAndRitualId(suin);
        if (companyStaffLiteDto.getSuin() == null) {
            return ResponseEntity.ok(
                    WsResponse.builder().status(WsResponse.EWsResponseStatus.FAILURE.getCode())
                            .body(WsError.builder().error(WsError.EWsError.APPLICANT_CHAT_CONTACT_NOT_FOUND.getCode()).build()).build());
        }
        return ResponseEntity.ok(WsResponse
                .builder()
                .status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                .body(companyStaffLiteDto).build());
    }


    @GetMapping("find-one/{ritualId}/{applicantUin}")
    public ResponseEntity<WsResponse<?>> findOneApplicantByUinAndRitualId(@PathVariable Long ritualId, Authentication authentication,
                                                                          @PathVariable String applicantUin) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        com.elm.shj.applicant.portal.services.integration.WsResponse response = chatContactService.findOneApplicantByUinAndRitualId(loggedInUserUin, ritualId, applicantUin);
        return ResponseEntity.ok(
                WsResponse.builder().status(response.getStatus())
                        .body(response.getBody()).build());
    }

    @GetMapping("/chat-list")
    public ResponseEntity<WsResponse<?>> listChatContactsWithLatestMessage(Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        List<ChatMessageLiteDto> chatMessageList = chatMessageService.listChatContactsWithLatestMessage(loggedInUserUin);

        return ResponseEntity.ok(WsResponse
                .builder()
                .status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                .body(chatMessageList).build());


    }

    @GetMapping("/messages/{contactId}")
    public ResponseEntity<WsResponse<?>> listMessages(@RequestParam(value = "page", defaultValue = "0") int page,
                                                      @RequestParam(value = "limit", defaultValue = "0") int limit,
                                                      @PathVariable long contactId,
                                                      @RequestParam(value = "time", defaultValue = "0") long time) {
        List<ChatMessageDto> chatMessageList = chatMessageService.listMessages(page, limit, contactId, time);

        return ResponseEntity.ok(WsResponse
                .builder()
                .status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                .body(chatMessageList).build());


    }

    @PostMapping("/save-chat-message/{senderId}/{receiverId}")
    public ResponseEntity<WsResponse<?>> saveSenderMessage(@RequestBody ChatMessageDto chatMessage, @PathVariable long senderId, @PathVariable long receiverId) {
        log.debug(Long.toString(chatMessage.getReceiver().getId()));
        chatMessage.getSender().setId(senderId);
        chatMessage.getReceiver().setId(receiverId);
        ChatMessageDto Message = chatMessageService.saveMessage(chatMessage);
        return ResponseEntity.ok(WsResponse
                .builder()
                .status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                .body(Message).build());
    }

}
