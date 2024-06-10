package umc.flo_clone_2ver.retrofit

interface SignUpView {
    fun onSignUpSuccess()

    fun onSignUpFailure(respMessage: String)

    // activity에서 상속받아서 사용
}