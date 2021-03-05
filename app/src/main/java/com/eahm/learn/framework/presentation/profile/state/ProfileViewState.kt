package com.eahm.learn.framework.presentation.profile.state

import android.os.Parcelable
import com.eahm.learn.business.domain.model.User
import com.eahm.learn.business.domain.state.ViewState
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileViewState(
    var updatedUser : User? = null // Updated User. Should return with a providerId (or empty if failed)
) : Parcelable, ViewState