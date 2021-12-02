/*
 * Copyright (c) 2021 ELM. All rights reserved.
 */
package com.elm.shj.applicant.portal.web.ws;

import com.elm.shj.applicant.portal.orm.entity.GenericWsResponse;
import com.elm.shj.applicant.portal.services.chat.ChatContactService;
import com.elm.shj.applicant.portal.services.dto.ApplicantChatContactVo;
import com.elm.shj.applicant.portal.web.navigation.Navigation;
import com.elm.shj.applicant.portal.web.security.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * List all chat contacts of a specific applicant by ritual ID.
     *
     * @param ritualId       the selected ritual ID
     * @param authentication the authenticated user
     * @return the list of chat contacts
     */
    @GetMapping("/{ritualId}")
    public ResponseEntity<WsResponse<?>> findChatContactsByUinAndRitualId(@PathVariable Long ritualId, Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        return ResponseEntity.ok(
                WsResponse.builder().status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                        .body(chatContactService.findChatContactsByUinAndRitualId(loggedInUserUin, ritualId)).build());
    }

    /**
     * Creates a new chat contact
     *
     * @param applicantRitualId the selected ritual ID
     * @param uin               the UIN of the chat contact applicant
     * @param alias             the alias of the chat contact applicant
     * @param mobileNumber      the mobile number of the chat contact applicant
     * @param contactAvatarFile the chat contact avatar file
     * @param authentication    the authenticated user
     * @return savedContact saved one
     */
    @PostMapping(value = "/create/{applicantRitualId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WsResponse<?>> create(@PathVariable Long applicantRitualId,
                                                @RequestPart String uin,
                                                @RequestPart String alias,
                                                @RequestPart(required = false) String mobileNumber,
                                                @RequestPart(value = "avatar", required = false) MultipartFile contactAvatarFile,
                                                Authentication authentication) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        ApplicantChatContactVo contactVo = ApplicantChatContactVo.builder().uin(uin).alias(alias).mobileNumber(mobileNumber).build();
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        if (contactAvatarFile != null && !contactAvatarFile.isEmpty() && contactAvatarFile.getSize() > 0) {
            builder.part("avatar", contactAvatarFile.getResource());
        }
        builder.part("contact", contactVo);
        return ResponseEntity.ok(WsResponse
                .builder()
                .status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                .body(chatContactService.createChatContact(loggedInUserUin, applicantRitualId, builder)).build());
    }

    /**
     * Updates user defined chat contact
     *
     * @param id                the ID number of the chat contact to be updated
     * @param alias             the alias of the chat contact applicant
     * @param mobileNumber      the mobile number of the chat contact applicant
     * @param contactAvatarFile the chat contact avatar file
     * @param authentication    the authenticated user
     * @return updatedContact updatedOne one
     */
    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WsResponse<?>> update(@PathVariable long id,
                                                @RequestPart String alias,
                                                @RequestPart String mobileNumber,
                                                @RequestPart(value = "avatar", required = false) MultipartFile contactAvatarFile,
                                                Authentication authentication) {
        ApplicantChatContactVo contactVo = ApplicantChatContactVo.builder().alias(alias).mobileNumber(mobileNumber).build();
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        if (contactAvatarFile != null && !contactAvatarFile.isEmpty() && contactAvatarFile.getSize() > 0) {
            builder.part("avatar", contactAvatarFile.getResource());
        }
        builder.part("contact", contactVo);
        return ResponseEntity.ok(WsResponse
                .builder()
                .status(WsResponse.EWsResponseStatus.SUCCESS.getCode())
                .body(chatContactService.updateChatContact(id, builder)).build());
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


    @GetMapping("find-one/{ritualId}/{applicantUin}")
    public ResponseEntity<WsResponse<?>> findOneApplicantByUinAndRitualId(@PathVariable Long ritualId, Authentication authentication,
                                                                       @PathVariable String applicantUin) {
        String loggedInUserUin = ((User) authentication.getPrincipal()).getUsername();
        GenericWsResponse response = chatContactService.findOneApplicantByUinAndRitualId(loggedInUserUin, ritualId, applicantUin);
        return ResponseEntity.ok(
                WsResponse.builder().status(response.getStatusCode())
                        .body(response.getBody()).build());

    }

}
